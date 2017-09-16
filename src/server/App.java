package server;

import dbService.DBService;

import java.io.IOException;
import java.util.Scanner;

public class App {

    private static DBService dbService;

    public static DBService getDBService()
    {
        if(dbService == null)
        {
            dbService = new DBService();
            return dbService;
        }
        else
        {
            return dbService;
        }
    }

    public static void main(String[] args) throws IOException {
        ChatServer chatServer = new ChatServer(9999);
        Thread thread = new Thread(chatServer);
        thread.start();
        Scanner scanner = new Scanner(System.in);
        String fromConsole = null;
        while (true) {
            System.out.println("Enter 'q' for exit");
            fromConsole = scanner.nextLine();
            if ("q".equals(fromConsole.trim().toLowerCase())){
                System.exit(0);
            }
        }
    }
}
