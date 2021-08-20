package src;

import java.io.Serializable;

public class Sala implements Comparable<Sala>, Serializable{
	private int numSala;
	private int capacidade;
	private boolean selected;

	public Sala(int numSala, int capacidade) {
		this.numSala = numSala;
		this.capacidade = capacidade;
		this.selected = false;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public int getNumSala() {
		return numSala;
	}

	public int getCapacidade() {
		return capacidade;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setNumSala(int numSala) {
		this.numSala = numSala;	
	}	

	public void setCapacidade(int capacidade) {
		this.capacidade = capacidade;
    }

	@Override
	public int compareTo(Sala sala) {
		if(this.numSala > sala.numSala){
			return 1;
		}
		if(this.numSala < sala.numSala){
			return -1;
		}
		return 0;
	}	
}
