package rpp;

public class tabuMemory {
	
	private int[][] mem;
	
	public tabuMemory(Problem p) {
		mem = new int[p.getRectangleSize()][p.getRectangleSize()];
	}
	
	public int getFrecuency(int i, int j) {
		if ((j > i) && (i != j))
			return mem[i][j];
		else
			return -1;
	}
	
	public void setFrecuency(int i, int j, int value) {
		if ((j > i) && (i != j))
			mem[i][j] = value;
	}
	
	public void incFrecuency(int i, int j) {
		if ((j > i) && (i != j))
			mem[i][j]++;		
	}
	
	public void decFrecuency(int i, int j) {
		if ((j > i) && (i != j))
			mem[i][j]--;		
	}
	
	public int getRecency(int i, int j) {
		if ((i > j) && (i != j))
			return mem[i][j];
		else
			return -1;
	}

	public void setRecency(int i, int j, int value) {
		if ((i > j) && (i != j))
			mem[i][j] = value;
	}
	
	public void incRecency(int i, int j) {
		if ((i > j) && (i != j))
			mem[i][j]++;		
	}
	
	public void decRecency() {
		for (int i = 0; i < mem.length; i++)
			for (int j = 0; j < i; j++)
				mem[i][j]--;
	}

	public void decRecency(int i, int j) {
		if ((i > j) && (i != j))
			mem[i][j]--;		
	}

}
