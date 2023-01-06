import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    public static void main(String[] args) {
        final File[] fileToSend = new File[1];

        JFrame jFrame = new JFrame("File Sharing");
        jFrame.setSize(500, 500);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(jFrame.EXIT_ON_CLOSE);

        JLabel jlTitle = new JLabel("Java Files Sender");
        jlTitle.setFont(new Font("Poppins", Font.BOLD, 25));
        jlTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel jlFileName = new JLabel("Choose file to send.");
        jlFileName.setFont(new Font("Poppins", Font.BOLD, 20));
        jlFileName.setBorder(new EmptyBorder(50, 0, 10, 0));
        jlFileName.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel jpButton = new JPanel();
        jpButton.setBorder(new EmptyBorder(75, 0, 10, 0));

        JButton jbSendFile = new JButton("Send File");
        jbSendFile.setPreferredSize(new Dimension(150, 75));
        jbSendFile.setFont(new Font("Poppins", Font.BOLD, 15));

        JButton jbChooseFile = new JButton("Choose File");
        jbChooseFile.setPreferredSize(new Dimension(150, 75));
        jbChooseFile.setFont(new Font("Poppins", Font.BOLD, 15));

        jpButton.add(jbSendFile);
        jpButton.add(jbChooseFile);

        jFrame.add(jlTitle);
        jFrame.add(jlFileName);
        jFrame.add(jpButton);
        jFrame.setVisible(true);

        jbChooseFile.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setDialogTitle("Choose a file to send.");

            if (jFileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                fileToSend[0] = jFileChooser.getSelectedFile();
                jlFileName.setText("You chose file -" + fileToSend[0].getName());
            }
        });

        jbSendFile.addActionListener(e -> {
            if (fileToSend[0] == null) {
                jlFileName.setText("Please choose a file to send.");
            } else {
                try {
                    FileInputStream fileInputStream = new FileInputStream(fileToSend[0].getAbsolutePath());
                    Socket socket = new Socket("localhost", 1234);

                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    String fileName = fileToSend[0].getName();
                    byte[] fileNameBytes = fileName.getBytes();

                    byte[] fileContentBytes = new byte[(int) fileToSend[0].length()];
                    fileInputStream.read(fileContentBytes);

                    dataOutputStream.writeInt(fileNameBytes.length);
                    dataOutputStream.write(fileNameBytes);

                    dataOutputStream.writeInt(fileContentBytes.length);
                    dataOutputStream.write(fileContentBytes);

                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

}
