import java.util.Arrays;

class IA {
	public int [][] plateau;
	Utilitaire utilitaire;
	Grille grille;

	public IA () {
		constructeur();
	}

	public IA (Grille grille) {
		this.grille = grille;
		constructeur();
	}

	public void constructeur() {
		utilitaire = new Utilitaire();
	}

	public Joueur place_bateaux_IA (Grille grille, int numero_du_joueur, boolean IA_humain, boolean carte_a_montrer) {
		int i;
		int tmp = 0;

		int nombre_de_gros_bateaux = 0;
		int tentative = 0;
		int tentative_2 = 0;
		Joueur res = null;
		Bateau bateau_tmp = new Bateau();
		int hauteur = grille.hauteur;
		int largeur = grille.largeur;
		int [] nombre_de_cases_bateaux;

		int nombre_de_bateaux = 0;

		int nombre_de_cases_max = bateau_tmp.nombre_de_place_de_bateau_max(grille);
		int min_hauteur_largeur = Math.min(hauteur, largeur);
		int nombre_cases_bateau_max;
		
		int nombre_de_cases_restantes = nombre_de_cases_max;
		while (nombre_de_cases_restantes>0) {
			nombre_de_cases_restantes-=min_hauteur_largeur;
			nombre_de_gros_bateaux++;
		}
		nombre_de_bateaux+=nombre_de_gros_bateaux;
		if ((-nombre_de_cases_restantes)>0) nombre_de_bateaux++;
		if (nombre_de_bateaux<1) nombre_de_bateaux = 1;
		nombre_de_cases_bateaux = new int[nombre_de_bateaux];

		for (i=0; i<nombre_de_bateaux; i++) {
			nombre_de_cases_restantes = nombre_de_cases_max;
			if (nombre_de_gros_bateaux>0) {
				nombre_cases_bateau_max = Math.min(nombre_de_cases_restantes, min_hauteur_largeur);
				//tmp = randInt(1, nombre_cases_bateau_max);
				//tmp = nombre_cases_bateau_max;
				tmp = randInt((int)((4.0/10)*nombre_cases_bateau_max), nombre_cases_bateau_max);
				if (tmp<1) tmp = 1;
				nombre_de_cases_bateaux[i] = tmp;
				nombre_de_cases_restantes-=tmp;
				nombre_de_gros_bateaux--;
			}
			else nombre_de_cases_bateaux[i] = Math.min(nombre_de_cases_restantes, min_hauteur_largeur);
			if (nombre_de_cases_bateaux[i]<1) nombre_de_cases_bateaux[i] = 1;
		}

		this.plateau = cree_plateau_tmp(grille);
		Bateau [] bateaux = new Bateau[nombre_de_bateaux];
		for (i=0; i<nombre_de_bateaux; i++) {
			bateaux[i] = null;
			tentative_2 = 0;
			while (bateaux[i] == null && tentative_2<5) {
				tentative = 0;
				while (bateaux[i] == null && tentative<5) {
					bateaux[i] = place_un_bateau(grille, nombre_de_cases_bateaux[i]);
					tentative++;
				}
				if (bateaux[i] == null) {
					aff("Bateaux numéro "+(i+1)+", "+tentative+" tentative(s) avec "+nombre_de_cases_bateaux[i]+" case(s).");
					nombre_cases_bateau_max = Math.min(nombre_de_cases_bateaux[i], min_hauteur_largeur);
					nombre_de_cases_bateaux[i] = randInt(1, nombre_cases_bateau_max);
					aff("Nouvel essai avec "+nombre_de_cases_bateaux[i]+" case(s).");
				}
				tentative_2++;
			}
			if (bateaux[i] == null) {
				aff("Bateaux numéro "+(i+1)+" dernier essai avec une seule case.");
				tentative = 0;
				nombre_de_cases_bateaux[i] = 1;
				while (bateaux[i] == null && tentative<5) {
					bateaux[i] = place_un_bateau(grille, nombre_de_cases_bateaux[i]);
					tentative++;
				}
			}
			if (bateaux[i] == null) {
				aff("Bateaux numéro "+(i+1)+" : échec avec une seule case.");
			}
			//afficher_plateau();
		}
		bateaux = adapte_le_nombre_de_bateaux(bateaux);
		res = new Joueur(numero_du_joueur, grille, bateaux, IA_humain, carte_a_montrer);

		return res;
	}

	/**
		Si certains bateaux sont nuls dans bateaux[], retourne un 
		nouveau tableau de Bateau sans les Bateau nuls.
	**/
	public Bateau [] adapte_le_nombre_de_bateaux (Bateau [] bateaux) {
		int i, h;
		Bateau [] bateaux_res;
		int nombre_de_bateaux_calcule = 0;

		for (i=0; i<bateaux.length; i++) {
			if (bateaux[i]!=null)
				nombre_de_bateaux_calcule++;
		}
		bateaux_res = new Bateau[nombre_de_bateaux_calcule];
		h = 0;
		for (i=0; i<bateaux.length; i++) {
			if (bateaux[i]!=null) {
				bateaux_res[h++] = bateaux[i];
			}
		}
		return bateaux_res;		
	}

	public boolean vertical_horizontal () {
		int tmp = randInt(0, 100);
		return (tmp<50);
	}

	public Bateau place_un_bateau (Grille grille, int taille_du_bateau) {
		boolean vertical_horizontal = vertical_horizontal ();
		return place_un_bateau_aux(grille, taille_du_bateau, vertical_horizontal);
	}

	/**
		Lorsqu'une case est touché par l'IA, elle essaie de coule le bateau entier si possible.
	**/
	public Case joue_coup_IA_avancee_2 () {
		return null;
	}

	/**
		vertical_horizontal :
			True : Vertical
			False : Horizontal
	**/
	public Bateau place_un_bateau_aux (Grille grille, int taille_du_bateau, boolean vertical_horizontal) {
		int i, j;

		Bateau res = null;
		int hauteur = grille.hauteur;
		int largeur = grille.largeur;
		int tmp2;
		Case case_tmp;
		Case [] cases_disponibles;
		Case [] cases = new Case[taille_du_bateau];

		cases_disponibles = cases_disponibles(vertical_horizontal, taille_du_bateau);
		if (cases_disponibles==null) {
			vertical_horizontal = inverse_bool(vertical_horizontal);
			cases_disponibles = cases_disponibles(vertical_horizontal, taille_du_bateau);
		}
		if (cases_disponibles==null) {
			//aff("IA - Place_un_bateau : Plus de cases disponibles");
			return null;
		}
		tmp2 = randInt(0, cases_disponibles.length-1);
		case_tmp = cases_disponibles[tmp2];
		int [] tmp3 = {case_tmp.ordonnee, case_tmp.abscisse};
		ajoute_un_bateau(tmp3, taille_du_bateau, vertical_horizontal);
		//afficher_plateau();
		case_tmp.abscisse+=1;
		case_tmp.ordonnee+=1;

		// Vertical
		if (vertical_horizontal) {
			j = 0;
			for (i=case_tmp.ordonnee; i<case_tmp.ordonnee+taille_du_bateau; i++) {
				cases[j++] = new Case(i, case_tmp.abscisse);
			}
		}
		// Horizontal
		else {
			j = 0;
			for (i=case_tmp.abscisse; i<case_tmp.abscisse+taille_du_bateau; i++) {
				cases[j++] = new Case(case_tmp.ordonnee, i);
			}
		}
		res = new Bateau(cases);

		return res;
	}

	public int [][] cree_plateau_tmp (Grille grille) {
		int i, j;
		int hauteur = grille.hauteur;
		int largeur = grille.largeur;
		
		int [][] tab = new int[hauteur][largeur];
		for (i=0; i<hauteur; i++) {
			for (j=0; j<largeur; j++) {
				tab[i][j] = 0;
			}
		}
		return tab;		
	}

	public void ajoute_un_bateau (int [] tab, int taille_bateau, boolean vertical_horizontal) {
		ajoute_un_bateau_aux (tab, taille_bateau, vertical_horizontal, true);
	}


	public void ajoute_un_bateau_aux (int [] tab, int taille_bateau, boolean vertical_horizontal, boolean distance_autour) {
		int i, j;

		int ordonnee = tab[0];
		int abscisse = tab[1];

		// Vertical
		if (vertical_horizontal) {
			for (i=ordonnee; i<ordonnee+taille_bateau; i++) {
				this.plateau[i][abscisse] = 1;
			}
			if (distance_autour) {
				if (ordonnee-1>=0)
					this.plateau[ordonnee-1][abscisse] = 1;
				if (ordonnee+taille_bateau<this.plateau.length)
					this.plateau[ordonnee+taille_bateau][abscisse] = 1;
				if (abscisse-1>=0) {
					for (i=ordonnee; i<ordonnee+taille_bateau; i++) {
						this.plateau[i][abscisse-1] = 1;
					}
				}
				if (abscisse+1<this.plateau[0].length) {
					for (i=ordonnee; i<ordonnee+taille_bateau; i++) {
						this.plateau[i][abscisse+1] = 1;
					}
				}
			}
		}
		// Horizontal
		else {
			for (i=abscisse; i<abscisse+taille_bateau; i++) {
				this.plateau[ordonnee][i] = 1;
			}
			if (distance_autour) {
				if (abscisse-1>=0)
					this.plateau[ordonnee][abscisse-1] = 1;
				if (abscisse+taille_bateau<this.plateau[0].length)
					this.plateau[ordonnee][abscisse+taille_bateau] = 1;
				if (ordonnee-1>=0) {
					for (i=abscisse; i<abscisse+taille_bateau; i++) {
						this.plateau[ordonnee-1][i] = 1;
					}
				}
				if (ordonnee+1<this.plateau.length) {
					for (i=abscisse; i<abscisse+taille_bateau; i++) {
						this.plateau[ordonnee+1][i] = 1;
					}
				}
			}
		}
	}

	public Case [] cases_disponibles (boolean vertical_horizontal, int taille_du_bateau) {
		int i, j, h;
		int tmp;
		int hauteur;
		int largeur = 0;
		boolean sature = false;
		int nombre_de_cases = 0;
		Case [] cases_disponibles_res;

		int [][] cases_disponibles = 
			cases_disponibles_aux_1(vertical_horizontal, taille_du_bateau);
		tmp = 0;
		for (i = 0; i<cases_disponibles.length; i++) {
			if (cases_disponibles[i]!=null)	{
				tmp++;
				largeur = cases_disponibles[i].length;
			}
		}
		if (tmp==0) {
			//aff("IA - cases_disponibles : plus de cases disponibles.");
			//aff("Détails : \n\tvertical_horizontal = "+vertical_horizontal);
			return null;
		}
		hauteur = cases_disponibles.length;
		nombre_de_cases = 0;
		for (i = 0; i<cases_disponibles.length; i++) {
			for (j=0; j<largeur; j++) {
				if (cases_disponibles[i]!=null)	nombre_de_cases++;
			}
		}
		cases_disponibles_res = new Case[nombre_de_cases];
		h = 0;
		for (i=0; i<hauteur; i++) {
			for (j=0; j<largeur; j++) {
				if (cases_disponibles[i]!=null) {
					// Vertical
					if (vertical_horizontal) {
						cases_disponibles_res[h] = new Case(j, i);
					}
					// Horizontal
					else {
						cases_disponibles_res[h] = new Case(i, j);
					}
					h++;
				}
			}
		}
		return cases_disponibles_res;
	}

	public int [][] cases_disponibles_aux_1 (boolean vertical_horizontal, int taille_du_bateau) {
		int i, j;
		int [] cases_disponibles_tmp;
		int hauteur = this.plateau.length;
		int largeur = this.plateau[0].length;
		int [] ligne_colonne;
		int [][] cases_disponibles;

		// Vertical
		if (vertical_horizontal) {
			ligne_colonne = new int[hauteur];
			cases_disponibles = new int[largeur][];
			for (i=0; i<largeur; i++) {
				for (j=0; j<hauteur; j++) {
					ligne_colonne[j] = this.plateau[j][i];
				}
				cases_disponibles_tmp = scanne_ligne_colonne(ligne_colonne, taille_du_bateau);
				cases_disponibles[i] = cases_disponibles_tmp;
			}
		}
		// Horizontal
		else {
			ligne_colonne = new int[largeur];
			cases_disponibles = new int[hauteur][];
			for (i=0; i<hauteur; i++) {
				for (j=0; j<largeur; j++) {
					ligne_colonne[j] = this.plateau[i][j];
				}
				cases_disponibles_tmp = scanne_ligne_colonne(ligne_colonne, taille_du_bateau);
				cases_disponibles[i] = cases_disponibles_tmp;
			}
		}
		return cases_disponibles;
	}

	public int [] scanne_ligne_colonne (int [] tab, int taille_du_bateau) {
		int i, j, h;
		boolean ok;
		int nombre_de_cases_possibles = 0;
		int [] cases_possibles = null;
		int taille_du_tableau = tab.length;

		for (i=0; i<taille_du_tableau; i++) {
			ok = true;
			if (i+taille_du_bateau<=taille_du_tableau) {
				for (j=i; j<i+taille_du_bateau; j++) {
					if (j<taille_du_tableau) {
						if (tab[j]==1) ok = false;
					}
				}
			}
			else ok = false;
			if (ok) nombre_de_cases_possibles++;
		}
		cases_possibles = new int[nombre_de_cases_possibles];
		h = 0;
		for (i=0; i<taille_du_tableau; i++) {
			ok = true;
			if (i+taille_du_bateau<=taille_du_tableau) {
				for (j=i; j<i+taille_du_bateau; j++) {
					if (j<taille_du_tableau) {
						if (tab[j]==1) ok = false;
					}
				}
			}
			else ok = false;
			if (ok) {
				cases_possibles[h] = i;
				h++;
			}
		}
		if (nombre_de_cases_possibles==0) {
			//aff("IA - scanne_ligne_colonne : aucune case possible : ");
			//aff("Détails : \n\ttaille_du_bateau = "+taille_du_bateau);
			//aff_tab(tab, "tab");
			return null;
		}
		else return cases_possibles;
	}

	public Case [] cases_occupees () {
		int i, j, h;

		Case [] res = null;
		int taille_res = 0;

		for (i=0; i<this.plateau.length; i++) {
			for (j=0; j<this.plateau[i].length; j++) {
				if (this.plateau[i][j]==1) taille_res++;
			}
		}
		res = new Case[taille_res];
		h = 0;
		for (i=0; i<this.plateau.length; i++) {
			for (j=0; j<this.plateau[i].length; j++) {
				if (this.plateau[i][j]==1) res[h] = new Case(i, j);
				h++;
			}
		}
		if (taille_res==0) return null;
		else return res;
	}

	public boolean joueur_un_coup (Joueur joueur_attaquant, Joueur joueur_attaque) {
		return joueur_attaquant.tenter_une_case_IA(joueur_attaque);
	}

	public boolean ligne_cases_possibles (int [] tab) {
		int i;

		for (i=0; i<tab.length; i++) {
			if (tab[i]!=0) return true;
		}
		return false;
	}

	public int [] ligne_cases_possibles_liste (int [] tab) {
		int i, j;
		int taille_res = 0;
		int [] res;

		for (i=0; i<tab.length; i++) {
			if (tab[i]!=0) taille_res++;
		}
		res = new int[taille_res];
		j = 0;
		for (i=0; i<tab.length; i++) {
			if (tab[i]!=0) {
				res[j] = i;
				j++;
			}
		}
		return res;
	}

	public Case [] liste_des_cases_possibles (Joueur joueur_attaquant) {
		int i, j, h;
		int taille_res = 0;
		Case [] res;

		int [][] plateau = joueur_attaquant.genere_plateau_cases_tentees(joueur_attaquant);

		for (i=0; i<plateau.length; i++) {
			for (j=0; j<plateau[i].length; j++) {
				if (plateau[i][j]==-1) taille_res++;
			}
		}
		res = new Case[taille_res];
		h = 0;
		for (i=0; i<plateau.length; i++) {
			for (j=0; j<plateau[i].length; j++) {
				if (plateau[i][j]==-1) {
					res[h] = new Case(i+1, j+1);
					h++;
				}
			}
		}
		return res;	
	}

	public Case genere_une_case_aleat (Joueur joueur_attaquant) {
		Case [] cases_possibles = liste_des_cases_possibles(joueur_attaquant);
		int choix = randInt(0, cases_possibles.length-1);
		return cases_possibles[choix];
	}

//################### Fonctions utilitaires ####################//

	public void afficher_plateau () {
		afficher_plateau_aux("du plateau (IA)", this.plateau);
	}

	public void afficher_plateau_aux (String nom, int [][] plateau) {
		int i, j;
		int [] t = null;
		if (plateau==null) return;
		if (plateau.length<1) return;
		int hauteur = plateau.length;
		int largeur = plateau[0].length;

		aff("\nAffichage "+nom+" : \n");
		for (i=0; i<hauteur; i++) {
			affnn("  ");
			for (j=0; j<largeur; j++) {
				affnn(""+plateau[i][j]+"   ");
			}
			affnn("\n");
		}
		affnn("\n");
	}

	public void aff_tab (int [] tab, String nom) {
		int i;

		if (tab==null) {
			aff("aff_tab : "+nom+" est null");
			return;
		}
		for (i=0; i<tab.length; i++) {
			aff(nom+"["+i+"] = "+tab[i]);
		}
	}

	public boolean inverse_bool (boolean tmp) {
		if (tmp) return false;
		else return true;
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

	public int randInt (int min, int max) {
		int tmp;

		if (min==max) return min;
		int res = (int)(Math.random()*max)+min;
		if (res>max) res = max;
		if (res<min) res = min;

		return res;
	}

	/**
		sauf est la tableau de valeurs 
		qui ne doit pas être retourné.
		Sauf est situé entre min et max non-inclus
	**/
	public int randInt_sauf (int min, int max, int [] sauf) {
		int i, j;
		int res;
		int tmp0;
		int taille_de_tmp2;
		int tmp3;

		if (sauf==null) return randInt(min, max);
		if (sauf.length==0) return randInt(min, max);

		// Tri le tableau sauf au cas où 
		// cela ne serait pas le cas.
		//utilitaire.aff_tab("sauf", sauf);
		Arrays.sort(sauf);
		utilitaire.aff_tab("sauf", sauf);

		int [] tmp1 = new int[sauf.length+1];
		int [] tmp2;
		if (min+1==sauf[0]) tmp1[0]=min;
		else tmp1[0] = randInt_trie_valeurs(min, sauf[0]);
		if (sauf.length>1) {
			j = 1;
			for (i=0; i<sauf.length-1; i++) {
				int tmp_12 = randInt_trie_valeurs(sauf[i], sauf[i+1]);
				aff("randInt_trie_valeurs("+sauf[i]+", "+sauf[i+1]+") = "+tmp_12);
				tmp1[j] = tmp_12;
				j++;
			}
		}
		if (max-1==sauf[sauf.length-1]) tmp1[tmp1.length-1]=max;
		else tmp1[tmp1.length-1] = randInt_trie_valeurs(sauf[sauf.length-1], max);
		//utilitaire.aff_tab("tmp1", tmp1);
		taille_de_tmp2 = 0;
		for (i=0; i<tmp1.length; i++) {
			if (tmp1[i]>0) taille_de_tmp2++;
		}
		if (taille_de_tmp2==0) {
			//aff("randInt_sauf : tous les chiffres entre min et max sont indisponibles (sont dans sauf)");
			return -1;
		}
		tmp2 = new int[taille_de_tmp2];
		j = 0;
		for (i=0; i<tmp1.length; i++) {
			if (tmp1[i]>0) {
				tmp2[j] = tmp1[i];
				j++;
			}
		}
		utilitaire.aff_tab("tmp2", tmp2);
		tmp3 = randInt(0, tmp2.length-1);
		res = tmp2[tmp3];
		if (res>max) res = max;
		if (res<min) res = min;
		return res;
	}

	/**
		Donne -1 si les valeurs min et max sont consécutives.
	**/
	public int randInt_trie_valeurs (int min, int max) {
		int tmp;
		if (min>max || min==max) {
			if (min>max) {
				//aff("randInt_trie_valeurs : Erreur, min>max");
			}
			else {
				//aff("min==max");
			}
			//aff("min = "+min);
			//aff("max = "+max);
			if ((max+1)==min) return -1;
		}
		if ((min+1)==max || min==max) return -1;
		else return randInt(min, max);
	}

	public void aff (String oo) {
		System.out.println(oo);
	}

	public void affnn (String oo) {
		System.out.print(oo);
	}
}