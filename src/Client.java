import java.net.*;
import java.io.*;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


public class Client extends JFrame {

    Socket socket;
    BufferedReader br;
    PrintWriter out;

    private final JLabel heading = new JLabel("Client Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN,20);

    public Client() {

        try {
            System.out.println("Connecting to server...");
            //IP Address of Server host, default: 127.0.0.1, ex: 192.168.0.01 for this both device should be connected to same network
            String ipAddress = "192.168.0.01";
            socket = new Socket(ipAddress, 7777);
            System.out.println("Connection established...");
            System.out.println("Enter the message: ");
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();
            startReading();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createGUI(){
        this.setTitle("Client Messenger[END]");
        this.setSize(600, 700);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Code for components

        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);

        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setFont(new Font("Roboto", Font.BOLD, 22));
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        this.setLayout(new BorderLayout());
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);
        messageInput.requestFocus();
        messageArea.setEditable(false);

        this.setVisible(true);
    }

    private void handleEvents(){
        messageInput.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                if (e.getKeyCode() == 10) {
                    String contentToSend = messageInput.getText();
                    messageArea.append("You: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");

                }

            }
        });
    }


    public void startReading(){
        Runnable r1 = ()->{
            System.out.println("Reader started...");
            try {
                while(true){
                    String msg = br.readLine();
                    if (msg.equals("exit")) {
                        JOptionPane.showMessageDialog(this, "Client terminated the chat...");
                        messageInput.setEnabled(false);
                        socket.close();
                        break;
                    }
                    messageArea.append("Client: "+msg+"\n");
                }
            } catch (Exception e) {
                System.out.println("Connection closed!");
            }
        };
        new Thread(r1).start();
    }


    public static void main(String[] args) {
        System.out.println("This is client...");
        new Client();
    }

}
  