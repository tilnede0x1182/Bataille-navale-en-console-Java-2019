
class Grille {
	public int hauteur;
	public int largeur;

	public Grille (int hauteur, int largeur) {
		this.hauteur = hauteur;
		this.largeur = largeur;
	}

// ################### Fonctions utilitaires ###################### //

	public String toString () {
		String res = "";
		res+="Hauteur = "+hauteur+"\n";
		res+="Largeur = "+largeur+"\n";
		return res;
	}

	public void aff (String oo) {
		System.out.println(oo);
	}

	public void affnn (String oo) {
		System.out.print(oo);
	}
}