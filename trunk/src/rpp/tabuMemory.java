package rpp;

public class tabuMemory {
	
	//Matriz en la que se guardan los periodos tabu (triangulo inferior) y las frecuencias (triangulo superior)  
	private int[][] mem;
	
	//Constructor
	public tabuMemory(Problem p) {
		mem = new int[p.getRectangleSize()][p.getRectangleSize()];
	}
	
	//Getter que devuelve la frecuencia de un movimiento
	public int getFrecuency(int i, int j) {
		if (j > i)
			return mem[i][j];
		else if (i > j)
			return mem[j][i];
		else
			return -1;
	}
	
	public void setFrecuency(int i, int j, int value) {
		if (j > i)
			mem[i][j] = value;
	}
	
	public void incFrecuency(int i, int j) {
		if (j > i)
			mem[i][j]++;
	}
	
	public void decFrecuency(int i, int j) {
		if (j > i)
			mem[i][j]--;		
	}
	
	public int getRecency(int i, int j) {
		if ((i > j) && (i != j))
			return mem[i][j];
		else if (j > i)
			return mem[j][i];  //El swap(1, 2) es el mismo que el swap(2, 1)
		else
			return -1;
	}

	public void setRecency(int i, int j, int value) {
		if (i > j)
			mem[i][j] = value;
	}
	
	public void incRecency(int i, int j) {
		if (i > j)
			mem[i][j]++;		
	}
	
	public void decRecency() {
		for (int i = 0; i < mem.length; i++)
			for (int j = 0; j < i; j++)
				if (mem[i][j] > 0)
					mem[i][j]--;
	}

	public void decRecency(int i, int j) {
		if (i > j)
			mem[i][j]--;		
	}

}
