package bus.chat;

public class StringMessage extends Message{
    private String text;

    public StringMessage(String author, String text) {
        this.setContentType(Message.TEXT_MESSAGE);
        this.setAuthor(author);
        this.text = text;
    }

    public String getText() {
        return this.text;
    }    
}
