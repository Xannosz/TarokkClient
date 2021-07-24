package hu.xannosz.tarokk.client.android.network;

import com.tisza.tarock.proto.MainProto.*;

public interface MessageHandler
{
	 void handleMessage(Message message);
	 void connectionError(ErrorType errorType);
	 void connectionClosed();

	enum ErrorType
	{
		VERSION_MISMATCH, INVALID_HELLO
	}
}
