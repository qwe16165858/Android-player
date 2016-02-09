package objects;

public class Msg implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	public int idx; // index
	public int pid;
	public CipherText cipher;

	public Msg() {
	}

	public Msg(int idx, int pid, CipherText cipher) {
		this.idx = idx;
		this.pid = pid;
		this.cipher = cipher;
	}
}
