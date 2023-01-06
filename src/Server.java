import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {
    public static List<MyFile> myFiles = new ArrayList<>();
    public static void main(String[] args) throws IOException {

        int fileId = 0;

        JFrame jFrame = new JFrame("Files Received");
        jFrame.setSize(500, 500);
        jFrame.setLayout(new BoxLayout(jFrame.getContentPane(), BoxLayout.Y_AXIS));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        JScrollPane jScrollPane = new JScrollPane(jPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

        JLabel jlTittle = new JLabel("File receiver");
        jlTittle.setFont(new Font("Poppins", Font.BOLD, 25));
        jlTittle.setBorder(new EmptyBorder(20, 0, 10, 0));
        jlTittle.setAlignmentX(Component.CENTER_ALIGNMENT);

        jFrame.add(jlTittle);
        jFrame.add(jScrollPane);
        jFrame.setVisible(true);

        ServerSocket serverSocket = new ServerSocket(1234);

        while(true) {
            try {
                Socket socket = serverSocket.accept();
                DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());

                int fileNameLength = dataInputStream.readInt();
                if (fileNameLength > 0) {
                    byte[] fileNameBytes = new byte[fileNameLength];
                    dataInputStream.readFully(fileNameBytes, 0, fileNameBytes.length);
                    String fileName = new String(fileNameBytes);

                    int fileContentLength = dataInputStream.readInt();
                    if (fileContentLength > 0) {
                        byte[] fileContentBytes = new byte[fileContentLength];
                        dataInputStream.readFully(fileContentBytes, 0, fileContentBytes.length);

                        JPanel jpFileRow = new JPanel();
                        jpFileRow.setLayout(new BoxLayout(jpFileRow, BoxLayout.Y_AXIS));

                        JLabel jlFilename = new JLabel(fileName);
                        jlFilename.setFont(new Font("Poppins", Font.BOLD, 25));
                        jlFilename.setBorder(new EmptyBorder(20, 0, 10, 0));
                        jlFilename.setAlignmentX(Component.CENTER_ALIGNMENT);

                        if (getFileExtension(fileName).equalsIgnoreCase("txt")) {
                            jpFileRow.setName(String.valueOf(fileId));

                            jpFileRow.addMouseListener(new MouseListener() {
                                @Override
                                public void mouseClicked(MouseEvent e) {
                                    JPanel jPanel1 = (JPanel) e.getSource();

                                    int fileId = Integer.parseInt(jPanel1.getName());

                                    for (MyFile file: myFiles) {
                                        if (file.getId() == fileId) {
                                            JFrame jfPreview = new JFrame("Preview");
                                            jfPreview.setSize(400, 400);

                                            JPanel jPanel2 = new JPanel();
                                            jPanel2.setLayout(new BoxLayout(jPanel2, BoxLayout.Y_AXIS));

                                            JLabel jfPreviewTitle = new JLabel("My File downloader");
                                            jfPreviewTitle.setFont(new Font("Poppins", Font.BOLD, 25));
                                            jfPreviewTitle.setBorder(new EmptyBorder(20, 0, 10, 0));
                                            jfPreviewTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

                                            JLabel jlPrompt = new JLabel("Are you sure you want to donwload this file? " + file.getFileName());
                                            jlPrompt.setFont(new Font("Poppins", Font.BOLD, 25));
                                            jlPrompt.setBorder(new EmptyBorder(20, 0, 10, 0));
                                            jlPrompt.setAlignmentX(Component.CENTER_ALIGNMENT);

                                            JButton jfPreviewYesBtn = new JButton("Yes");
                                            jfPreviewYesBtn.setPreferredSize(new Dimension(150, 75));
                                            jfPreviewYesBtn.setFont(new Font("Poppins", Font.BOLD, 15));

                                            JButton jfPreviewNoBtn = new JButton("Yes");
                                            jfPreviewNoBtn.setBackground(Color.RED);
                                            jfPreviewNoBtn.setPreferredSize(new Dimension(150, 75));
                                            jfPreviewNoBtn.setFont(new Font("Poppins", Font.BOLD, 15));

                                            JLabel jlFileContent = new JLabel();
                                            jlFileContent.setAlignmentX(Component.CENTER_ALIGNMENT);

                                            JPanel jpBtns = new JPanel();
                                            jpBtns.setBorder(new EmptyBorder(20, 0, 10, 0));
                                            jpBtns.add(jfPreviewYesBtn);
                                            jpBtns.add(jfPreviewNoBtn);

                                            jfPreview.setVisible(true);

                                            if (getFileExtension(fileName).equalsIgnoreCase("txt")) {
                                                jlFileContent.setText("<html>" + new String(file.getData()) + "</html");
                                            } else {
                                                jlFileContent.setIcon(new ImageIcon(file.getData()));
                                            }

                                            jfPreviewYesBtn.addActionListener(f -> {
                                                File fileToDownload = new File(fileName);

                                                try {
                                                    FileOutputStream fileOutputStream = new FileOutputStream(fileToDownload);
                                                    fileOutputStream.write(file.getData());
                                                    fileOutputStream.close();

                                                    jfPreview.dispose();
                                                } catch (IOException ex) {
                                                    ex.printStackTrace();
                                                }
                                            });

                                            jfPreviewNoBtn.addActionListener(g -> {
                                                jfPreview.dispose();
                                            });

                                            jPanel2.add(jfPreviewTitle);
                                            jPanel2.add(jlPrompt);
                                            jPanel2.add(jlFileContent);
                                            jPanel2.add(jpBtns);
                                            jfPreview.add(jPanel2);
                                        }
                                    }
                                }

                                @Override
                                public void mousePressed(MouseEvent e) {

                                }

                                @Override
                                public void mouseReleased(MouseEvent e) {

                                }

                                @Override
                                public void mouseEntered(MouseEvent e) {

                                }

                                @Override
                                public void mouseExited(MouseEvent e) {

                                }
                            });

                            jpFileRow.add(jlFilename);
                            jPanel.add(jpFileRow);
                            jFrame.validate();
                        }

                        myFiles.add(new MyFile(fileId, fileName, fileContentBytes, getFileExtension(fileName)));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String getFileExtension(String fileName) {
        int i = fileName.lastIndexOf(".");

        if (i > 0) {
            return fileName.substring(i + 1);
        } else {
            return "No extension found";
        }
    }
}
