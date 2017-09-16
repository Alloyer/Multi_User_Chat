package client;

import dbService.DBException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ChatClientLoginFrame extends JFrame {

    private JTextField loginTextField;

    private JLabel loginLabel;

    private JTextField serverAddressTextField;

    private JLabel serverAddressLabel;

    private JPasswordField passwordField;

    private JLabel passwordLabel;

    private JButton loginButton;

    private Image image = getImage("https://support.apple.com/library/APPLE/APPLECARE_ALLGEOS/Product_Help/ru_RU/PUBLIC_USERS/134327/IC_messages_64.png");

    public ChatClientLoginFrame() throws HeadlessException {
        ChatClientLoginFrame loginFrame = this;

        this.setTitle("Login");
        this.setSize(300, 200);
        this.setLocationRelativeTo(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setIconImage(image == null ? (new ImageIcon("C:\\Users\\alloyer\\IdeaProjects\\Java2Lesson3\\resource\\Icon.png")).getImage() : image);

        Font font = new Font("Arial", Font.PLAIN, 20);

        this.loginLabel = new JLabel("Login:");
        this.loginTextField = new JTextField();
        this.serverAddressLabel = new JLabel("Server address:");
        this.serverAddressTextField = new JTextField();
        this.passwordLabel = new JLabel("Password:");
        this.passwordField = new JPasswordField();
        this.loginButton = new JButton("Login");

        this.loginButton.setFont(font);
        this.loginLabel.setFont(font);
        this.loginTextField.setFont(font);
        this.serverAddressLabel.setFont(font);
        this.serverAddressTextField.setFont(font);
        this.passwordLabel.setFont(font);
        this.passwordField.setFont(font);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new GridLayout(3, 2));

        jPanel.add(this.loginLabel);
        jPanel.add(this.loginTextField);
        jPanel.add(this.serverAddressLabel);
        jPanel.add(this.serverAddressTextField);
        jPanel.add(this.passwordLabel);
        jPanel.add(this.passwordField);

        this.add(jPanel, BorderLayout.CENTER);
        this.add(this.loginButton, BorderLayout.SOUTH);

        this.loginButton.addActionListener(e -> {
            String login = null;
            String host = null;
            Integer port = null;

            login = loginTextField.getText().trim();
            if (login.length() == 0) {
                error("Please enter login");
                return;
            }
            String address = serverAddressTextField.getText().trim();
            if (address.length() == 0) {
                error("Please enter server host and port");
                return;
            }
            if (address.indexOf(':') < 1) {
                error("Please enter server port after ':' example: localhost:9090");
                return;
            }
            String[] split = address.split(":");
            host = split[0];
            try {
                port = Integer.valueOf(split[1]);
            } catch (RuntimeException ex) {
                error("Please enter server port after ':' example: localhost:9090");
                return;
            }
            ChatClient chatClient;

            try
            {
                chatClient = new ChatClient(host, port);
            }
            catch (IOException ioe)
            {
                error(String.format("Can't connect to server by adress %s:%s", host, port));
                return;
            }

            try
            {
                server.App.getDBService().addUser(login, passwordField.getPassword().toString());
            }
            catch (DBException DBe)
            {
                DBe.printStackTrace();
            }

            try
            {
                if (!chatClient.login(login))
                {
                    error(String.format("Can't connect to server by login %s", login));
                    return;
                }
            }
            catch (IOException e1)
            {
                error("Connection error. Try later.");
                return;
            }

            ChatClientFrame chatClientFrame = new ChatClientFrame(login, loginFrame, chatClient);
            chatClientFrame.setVisible(true);
            loginFrame.setVisible(false);
        });
    }

    private void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private Image getImage(String urlString) {
        try
        {
            URL url = new URL(urlString);
            BufferedImage bufferedImage = ImageIO.read(url);
            ImageIcon imageIcon = new ImageIcon(bufferedImage);
            Image image = imageIcon.getImage();
            return image;
        }
        catch (Throwable e)
        {
            System.err.println("Unable to load picture for login frame. Default will be set.");
        }
        return null;
    }
}
