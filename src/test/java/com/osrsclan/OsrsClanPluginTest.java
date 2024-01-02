package com.osrsclan;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class OsrsClanPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(OsrsClanPlugin.class);
		RuneLite.main(args);
	}
}