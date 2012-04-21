package to.joe.j2mc.globalbans.mcbouncer;

public class NoteResult {
    public boolean success;
    public String error;
    public int totalcount;
    public int page;
    public Note data;
    public class Note {
        public int noteid;
        public String username;
        public String issuer;
        public String server;
        public String note;
        public String time;
        public boolean global;
    }
}
