package bus.chat;

import java.io.Serializable;

public abstract class Message implements Serializable {
    public final static int TEXT_MESSAGE = 0x10;
    public final static int FILE_MESSAGE = 0x20;

    private int contentType;
    private String author;

    public Message() {
        this.contentType = 0x0;
    }

    public void setContentType(int contentType) {
        this.contentType = contentType;
    }

    public int getContentType() {
        return this.contentType;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthor() {
        return this.author;
    }
}

