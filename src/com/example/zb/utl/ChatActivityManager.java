package com.example.zb.utl;

import com.example.beihangQA_test.ChatActivity;

public class ChatActivityManager {
//	private static ChatActivity currentActivity = null;
//
//	public static ChatActivity getCurrentActivity() {
//		return currentActivity;
//	}
//
//	public static void setCurrentActivity(ChatActivity currentActivity) {
//		ChatActivityManager.currentActivity = currentActivity;
//	}
	private static ChatActivity currentActivity=null;
	public static String aimId="null";
	public static ChatActivity getCurrentActivity()
	{
		return currentActivity;
	}
	public static void setCurrentActivity(ChatActivity currentActivity,String aimID)
	{
		ChatActivityManager.currentActivity=currentActivity;
		aimId=aimID;
	}

}
