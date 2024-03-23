package org.example;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class Database {
    private Map<String, List<Program>> channelsAndPrograms = new HashMap<>();
    private List<Program> allPrograms = new ArrayList<>();

    public void readDataFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            String channel = "";
            BroadcastsTime time = null;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    channel = line.substring(1);
                } else {
                    if (time == null) {
                        String[] timeParts = line.split(":");
                        time = new BroadcastsTime(Byte.parseByte(timeParts[0]), Byte.parseByte(timeParts[1]));
                    } else {
                        String name = line;
                        Program program = new Program(channel, time, name);
                        allPrograms.add(program);
                        channelsAndPrograms.computeIfAbsent(channel, k -> new ArrayList<>()).add(program);
                        time = null;
                    }
                }
            }
            System.out.println(channelsAndPrograms.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showAllPrograms() {
        allPrograms.sort(Comparator.comparing(Program::getTime));
        for (Program program : allPrograms) {
            System.out.println(program.getChannel() + " " + program.getTime() + " " + program.getName());
        }
    }

    public void showProgramNow() {
        LocalTime currentTime = LocalTime.now();
        for (Program program : allPrograms) {
            if (program.getTime().after(new BroadcastsTime((byte) currentTime.getHour(), (byte) currentTime.getMinute()))) {
                System.out.println("Сейчас идет: " + program.getChannel() + " " + program.getTime() + " " + program.getName());
                break;
            }
        }
    }

    public List<Program> searchName(String name) {
        List<Program> foundedPrograms = new ArrayList<>();
        for (Program program : allPrograms) {
            if (program.getName().equalsIgnoreCase(name)) {
                foundedPrograms.add(program);
            }
        }
        return foundedPrograms;
    }

    public List<Program> programsOfChannel(String name) {
        List<Program> foundPrograms = new ArrayList<>();
        List<Program> channelPrograms = channelsAndPrograms.get(name);
        if (channelPrograms != null) {
            for (Program program : channelPrograms) {
                foundPrograms.add(program);
            }
        }
        return foundPrograms;
    }

    public List<Program> programsOfInterval(String name, BroadcastsTime timeBegin, BroadcastsTime timeEnd) {
        List<Program> foundedPrograms = new ArrayList<>();
        List<Program> channelPrograms = channelsAndPrograms.get(name);
        if (channelPrograms != null) {
            for (Program program : channelPrograms) {
                if (program.getTime().between(timeBegin, timeEnd)) {
                    foundedPrograms.add(program);
                }
            }
        }
        return foundedPrograms;
    }
}