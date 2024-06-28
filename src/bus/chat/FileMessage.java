package bus.chat;

public class FileMessage extends Message{
    private String filename;
    private long filesize;
    private byte[] content;

    public FileMessage (String author, String filename, long filesize, byte[] content) {
        this.setContentType(Message.FILE_MESSAGE);
        this.setAuthor(author);
        this.filename = filename;
        this.filesize = filesize;
        this.content = content;
    }

    public String getFilename() {
        return filename;
    }

    public long getFilesize() {
        return filesize;
    }

    public byte[] getContent() {
        return content;
    }
}
