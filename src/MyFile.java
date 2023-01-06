public class MyFile {
    private int id;
    private String fileName;
    private byte[] Data;
    private String fileExtension;

    public MyFile(int id, String fileName, byte[] data, String fileExtension) {
        this.id = id;
        this.fileName = fileName;
        Data = data;
        this.fileExtension = fileExtension;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getData() {
        return Data;
    }

    public void setData(byte[] data) {
        Data = data;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public void setFileExtension(String fileExtension) {
        this.fileExtension = fileExtension;
    }
}
