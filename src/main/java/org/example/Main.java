package org.example;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Database database = new Database();
        database.readDataFromFile("schedule.txt");
        database.showAllPrograms();
        database.showProgramNow();
        System.out.println(database.searchName("Умницы и умники"));
        System.out.println(database.programsOfChannel("Первый"));
        BroadcastsTime time1 = new BroadcastsTime((byte) 12, (byte) 14);
        BroadcastsTime time2 = new BroadcastsTime((byte) 20, (byte) 14);
        System.out.println(database.programsOfInterval("Первый", time1, time2));

    }
}