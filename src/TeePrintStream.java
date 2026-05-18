import java.io.OutputStream;
import java.io.PrintStream;

/**
 * A PrintStream that mirrors every byte it receives to a second
 * PrintStream as well. Used in Task B to duplicate console output
 * into task_b_output.txt without rewriting every println call.
 */
public class TeePrintStream extends PrintStream {
    private final PrintStream second;

    public TeePrintStream(OutputStream main, PrintStream second) {
        super(main, true);
        this.second = second;
    }

    @Override
    public void write(int b) {
        super.write(b);
        second.write(b);
    }

    @Override
    public void write(byte[] buf, int off, int len) {
        super.write(buf, off, len);
        second.write(buf, off, len);
    }

    @Override
    public void flush() {
        super.flush();
        second.flush();
    }
}
