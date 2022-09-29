class Case {
	public int ordonnee;
	public int abscisse;
	public Joueur joueur;
	public Bateau bateau;
	public int numero_du_bateau;

	public boolean attaquee;
	public boolean coule;

	/**
		Pour le tableau d'attaque :
		0 : tans l'eau
		1 : touché
		2 : coulé
	**/
	public int etat;

	public Case() {}

	public Case (int ordonnee, int abscisse) {
		constructeur(ordonnee, abscisse);
		this.etat = -1;
	}

	public Case (Joueur joueur, Bateau bateau, int numero_du_bateau, 
						int ordonnee, int abscisse) {
		this.joueur = joueur;
		this.bateau = bateau;
		this.numero_du_bateau = numero_du_bateau;
		constructeur(ordonnee, abscisse);
		this.etat = -1;
	}

	public Case (int ordonnee, int abscisse, int etat) {
		constructeur(ordonnee, abscisse);
		this.etat = etat;
	}

	public void constructeur (int ordonnee, int abscisse) {
		this.ordonnee = ordonnee;
		this.abscisse = abscisse;
		this.attaquee = false;
		this.coule = false;
	}

// ################ Fonctions utilitaires du jeu ################ //

	public boolean est_attaquee() {
		return attaquee;
	}

	public boolean est_coulee() {
		return coule;
	}

	public void attaquer () {
		attaquee = true;
	}

	public void couler () {
		coule = true;
	}

	/**
		Format correct : A5.
	**/
	public boolean verifie_format_case (String s0, int hauteur) {
		char tmp, tmp2;
		if (s0==null) return false;
		if (s0.length()==2) {
			tmp = s0.charAt(0);
			if (tmp>='A' && tmp<=('A'+(hauteur-1))) {
				tmp2 = s0.charAt(1);
				if (is_integer(""+tmp2)) return true;
				else return false;
			} else return false;
		} else return false;
	}

	/**
		On donne des coordonnées type A5
		et la fonction donne 1, 5 pour A5
	**/
	public int [] convertit_case_en_coordonnee (Grille grille, String s0) {
		int [] res = new int[2];
		char c='A';
		int ordonnee;
		int abscisse;

		if (verifie_case_existe(grille, s0)) {
			ordonnee = (int)(s0.charAt(0)-(int)('A')+1);
			abscisse = (int)(s0.charAt(1)-(int)('0'));
			res[0] = ordonnee;
			res[1] = abscisse;
		}
		else {
			aff("convertit_case_en_coordonnee : Format incorrect");
			return null;
		}

		return res;
	}


	public boolean verifie_case_existe (Grille grille, String case_tmp) {
		if (case_tmp==null) return false;
		if (case_tmp.isEmpty()) return false;
		if (case_tmp.length()!=2) return false;
		if (!verifie_format_case(case_tmp, grille.hauteur)) return false;
		int abscisse = (int)(case_tmp.charAt(1)-'1'+1);
		if (abscisse<1 || abscisse>grille.largeur) return false;
		return true;
	}

	/**
		Prend la première case et la dernière case
		et retourne un tableau de cases contenant
		toutes les cases du bateau.
	**/
	public void convertit_case (Case case_1, Case case_fin) {
		
	}

	/**
		Renvoie true si l'une des cases de tab2 est dans tab1.

	**/
	public boolean contient_case (Case [] tab1, Case[] tab2) {
		int i, j;
		Case case_tmp = new Case();

		for (i=0; i<tab1.length; i++) {
			for (j=0; j<tab2.length; j++) {
				if (case_tmp.compare_case(tab1[i], tab2[j])) return true;
			}
		}
		return false;
	}

// ################### Fonctions utilitaires #################### //

	public boolean compare_case (Case case_1, Case case_2) {
		return (case_1.ordonnee==case_2.ordonnee &&
			case_1.abscisse==case_2.abscisse);
	}

	public boolean contient_case (Case [] cases, Case case_a_comparer) {
		int i;

		for (i=0; i<cases.length; i++) {
			if (compare_case(cases[i], 
				case_a_comparer)) return true;
		}
		return false;
	}

	public boolean is_integer (String s0) {
		try {
			int n1 = Integer.parseInt(s0);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	public String aff_case_str () {
		String res="";
		res+="Ordonnee = "+ordonnee+"\n";
		res+="Abscisse = "+abscisse+"\n";
		return res;
	}

	/**
		Donne les coordonnées de la case en format type : A5
	**/
	public String case_format_ecrit() {
		return (new Utilitaire()).convertit_coordonnees_str(ordonnee, abscisse);
	}

	public String toString() {
		String res = "";
		if (joueur!=null) {
			res = "J"+joueur.toString()+"B"+this.numero_du_bateau;
			if (attaquee && !coule) {
				res = "T"+res;
			}
			if (attaquee && coule) {
				res = "C"+res;
			}
		}
		else {
			if (attaquee) {
				res = "X  ";
			}
			else res = "0  ";
		}
		return res;
	}

	public void aff (String oo) {
		System.out.println(oo);
	}

	public void affnn (String oo) {
		System.out.print(oo);
	}

}