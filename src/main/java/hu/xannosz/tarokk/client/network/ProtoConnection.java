package hu.xannosz.tarokk.client.network;

import com.tisza.tarock.proto.MainProto;
import com.tisza.tarock.proto.MainProto.Message;
import hu.xannosz.microtools.Sleep;
import hu.xannosz.tarokk.client.util.Util;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ProtoConnection implements Closeable {
    private static final String HELLO_STRING = "Tarokk";
    private static final int VERSION = 8;
    private static final int KEEP_ALIVE_DELAY = 1;
    private static final int SOCKET_TIMEOUT = 10;

    private final Executor messageHandlerExecutor;
    private Socket socket;
    private final InputStream is;
    private final OutputStream os;
    private final Object packetHandlersLock = new Object();
    private final List<MessageHandler> packetHandlers = new ArrayList<>();
    private final BlockingQueue<Message> messagesToSend = new LinkedBlockingQueue<>();
    private volatile boolean started = false;
    private volatile boolean closeRequested = false;

    private final Thread readerThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                byte[] helloArr = new byte[10];
                if (is.read(helloArr) == 0) {
                    Util.Log.logError("Hello string reading not success.");
                }
                ByteBuffer helloByteBuffer = ByteBuffer.wrap(helloArr);
                String serverHelloString = new String(helloArr, 0, 6);
                int serverVersion = helloByteBuffer.getInt(6);

                if (!serverHelloString.equals(HELLO_STRING)) {
                    error(ErrorType.INVALID_HELLO);
                } else if (serverVersion != VERSION) {
                    error(ErrorType.VERSION_MISMATCH);
                }

                while (!closeRequested) {
                    Message message = Message.parseDelimitedFrom(is);

                    if (message == null)
                        break;

                    if (message.getMessageTypeCase() == Message.MessageTypeCase.KEEPALIVE)
                        continue;

                    synchronized (packetHandlersLock) {
                        for (MessageHandler handler : packetHandlers) {
                            messageHandlerExecutor.execute(() -> handler.handleMessage(message));
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (!closeRequested) {
                    try {
                        close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });

    private final Thread writerThread = new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                while (!closeRequested) {
                    try {
                        Message message = messagesToSend.poll(KEEP_ALIVE_DELAY, TimeUnit.SECONDS);

                        if (message == null)
                            message = MainProto.Message.newBuilder().setKeepAlive(MainProto.KeepAlive.getDefaultInstance()).build();

                        message.writeDelimitedTo(os);
                    } catch (InterruptedException e) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (!closeRequested) {
                    try {
                        close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    });

    public ProtoConnection(Socket socket, Executor messageHandlerExecutor) throws IOException {
        this.socket = socket;
        this.messageHandlerExecutor = messageHandlerExecutor;

        socket.setTcpNoDelay(true);
        socket.setSoTimeout(SOCKET_TIMEOUT * 1000);

        is = socket.getInputStream();
        os = socket.getOutputStream();
    }

    private void error(ErrorType errorType) throws IOException {
        synchronized (packetHandlersLock) {
            Util.Log.logError("Error: " + errorType);
        }
        close();
    }

    public synchronized void start() {
        if (started || closeRequested)
            throw new IllegalStateException();

        started = true;
        readerThread.start();
        writerThread.start();
    }

    public void addMessageHandler(MessageHandler handler) {
        synchronized (packetHandlersLock) {
            packetHandlers.add(handler);
        }
    }

    public void sendMessage(Message message) {
        if (!messagesToSend.offer(message)) {
            Util.Log.logError("Message sending not success.");
        }
    }

    private void stopThreads() {
        stopThread(readerThread);
        stopThread(writerThread);
    }

    private void stopThread(Thread thread) {
        if (!thread.isAlive())
            return;

        if (thread == Thread.currentThread())
            return;

        thread.interrupt();

        while (thread.isAlive()) {
            Sleep.sleepMillis(100);
        }
    }

    public synchronized void close() throws IOException {
        if (closeRequested)
            return;

        closeRequested = true;

        stopThreads();

        if (socket != null) {
            socket.close();
            socket = null;
        }

        synchronized (packetHandlersLock) {
            Util.Log.logMessage("Connection closed!");
        }
    }

    public interface MessageHandler {
        void handleMessage(Message message);
    }

    private enum ErrorType {
        VERSION_MISMATCH, INVALID_HELLO
    }
}
