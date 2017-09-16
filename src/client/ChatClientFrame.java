package client;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ChatClientFrame extends JFrame implements ChatClient.ReceiveMessage {

    private JTextArea messageTextArea;
    private JTextField sendTextField;
    private JButton sendButton;

    private String login;

    private ChatClientLoginFrame loginFrame;

    private ChatClient chatClient;

    private Image image = getImage("https://support.apple.com/library/APPLE/APPLECARE_ALLGEOS/Product_Help/ru_RU/PUBLIC_USERS/134327/IC_messages_64.png");

    public ChatClientFrame(String login, ChatClientLoginFrame loginFrame, ChatClient chatClient) throws HeadlessException {
        this.login = login;
        this.loginFrame = loginFrame;
        this.chatClient = chatClient;

        this.chatClient.setReceiveMessage(this);

        this.setTitle(String.format("Login:%s", this.login));
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(400, 400);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout());

        Font font = new Font("Arial", Font.PLAIN, 20);
        Color color = new Color(193, 193, 200);

        this.messageTextArea = new JTextArea();
        JScrollPane jScrollPane = new JScrollPane(this.messageTextArea);
        JPanel sendPanel = new JPanel(new BorderLayout());
        this.sendTextField = new JTextField();
        this.sendButton = new JButton("SEND");

        this.setIconImage(image == null ? (new ImageIcon("C:\\Users\\alloyer\\IdeaProjects\\Java2Lesson3\\resource\\Icon.png")).getImage() : image);

        this.messageTextArea.setFont(font);
        this.sendTextField.setFont(font);
        this.sendButton.setFont(font);

        this.messageTextArea.setBackground(color);
        this.messageTextArea.setDisabledTextColor(Color.BLACK);
        this.sendTextField.setBackground(color);

        this.messageTextArea.setEditable(false);
        this.messageTextArea.setLineWrap(true);
        this.messageTextArea.setWrapStyleWord(true);

        sendPanel.add(this.sendTextField, BorderLayout.CENTER);
        sendPanel.add(this.sendButton, BorderLayout.EAST);

        this.sendTextField.addActionListener(e -> sendText());
        this.sendButton.addActionListener(e -> sendText());

        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(sendPanel, BorderLayout.PAGE_END);
    }

    private void sendText() {
        String text = this.sendTextField.getText();
        if (text.length() == 0) {
            return;
        }
        try {
            this.chatClient.sendMessage(text);
        } catch (IOException e) {
            error("Connection to server error. Try later.");
            this.loginFrame.setVisible(true);
            this.setVisible(false);
        }
        this.sendTextField.setText("");
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
            System.err.println("Unable to load picture for chat frame. Default will be set.");
        }
        return null;
    }

    @Override
    public synchronized void receiveMessage(String message) {
        this.messageTextArea.append(message);
        this.messageTextArea.append("\n");
    }

    private void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
