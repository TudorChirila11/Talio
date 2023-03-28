package server;

public class OutgoingMessage {
    private String content;

    public OutgoingMessage(String content) {this.content = content;}

    /**
     * constructor for the OutgoingMessage
     */
    public OutgoingMessage() {}

    public String getContent() {return content;}

    /**
     * A setter for the content variable
     * @param content the string that'll replace the current content
     */
    public void setContent(String content) {
        this.content = content;
    }
}
