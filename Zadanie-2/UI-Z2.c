//Zadanie c.2, predmet Umela Inteligencia
//Stanislav Jakubek, 2. rocnik, 2017/2018

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define DEBUG 0

typedef struct pozicia {
	int farba;
	int dlzka;
	int x;
	int y;
	char smer;
}POZICIA;

typedef struct tah {
	int farba;
	int dlzka;
	int oldx;
	int oldy;
	int newx;
	int newy;
	char smer;
}TAH;

typedef struct uzol {
	int **krizovatka;
	int hlbka;
	POZICIA **pozicie;
	TAH *tah;
	struct uzol *pred;
}UZOL;

//inicializacia pola arr o velkosti A x B
void init(int **arr, int A, int B) {
	int i, j;
	
	for (i = 0; i < A; i++) {
		for (j = 0; j < B; j++) {
			arr[i][j] = 0;
		}
	}
}

// kontrola zhodnosti poli 6 x 6
// vrati 0 ak sa nezhoduju, 1 ak ano
int checkArrays(int **arr1, int **arr2) {
	int i, j;
	
	for (i = 0; i < 6; i++) {
		for (j = 0; j < 6; j++) {
			if (arr1[i][j] != arr2[i][j]) return 0;
		}
	}
	
	return 1;
}

//kontrola ci je pole arr v koncovom stave
//vrati 0 ak nie je, 1 ak je v koncovom stave
int checkResult(int **arr, int n) {
	int i, j, k;
	
	for (i = 0; i < 6; i++) {
		for (j = 0; j < 6; j++) {
			if (arr[i][j] == n) {
				for (k = j; k < 6; k++) {
					if (arr[i][k] != n) return 0;
					if ((k == 5) && (arr[i][k] == n)) return 1;
				}
			}
		}
	}
	
	return 0;
}

//vypis pola arr o velkosti 6x6
void printArray(int **arr) {
	int i, j;
	
	for (i = 0; i < 6; i++) {
		for (j = 0; j < 6; j++) {
			printf("%d ", arr[i][j]);
		}
		printf("\n");
	}
}

//kontrola moznosti tahu
//vrati 0 ak nie je mozne tah vykonat, 1 ak ano
int checkMove(int **arr, TAH *tah) {
	int i;
	
	if (DEBUG) printf("checkMove\n");
	
	//kontrola priestoru medzi povodnou a novou poziciou
	if (tah->smer == 'h') {
		//horizontalna kontrola
		if (tah->newx > tah->oldx) {
			//kladny posun
			if (DEBUG) {
				printf("tah->smer: %c\n", tah->smer);
				printf("tah->oldy: %d\n", tah->oldy);
				printf("tah->newy: %d\n", tah->newy);
				printf("tah->oldx: %d\n", tah->oldx);
				printf("tah->newx: %d\n", tah->newx);
				printf("tah->dlzka: %d\n", tah->dlzka);
				printf("arr:\n");
				printArray(arr);
			}
			for (i = tah->oldx; i < tah->newx + tah->dlzka; i++) {
				if (DEBUG) {
					printf("i: %d\n", i);
					printf("arr[tah->oldy][i] %d\n", arr[tah->oldy][i]);
				}
				if (!((arr[tah->oldy][i] == 0) || (arr[tah->oldy][i] == tah->farba))) {
					//ak nie je pravda ze prvok == 0 alebo ID farby
					if (DEBUG) printf("NEPLATI ");
					return 0;
				}
			}
		}
		else {
			//zaporny posun
			if (DEBUG) {
				printf("tah->smer: %c\n", tah->smer);
				printf("tah->oldy: %d\n", tah->oldy);
				printf("tah->newy: %d\n", tah->newy);
				printf("tah->oldx: %d\n", tah->oldx);
				printf("tah->newx: %d\n", tah->newx);
				printf("tah->dlzka: %d\n", tah->dlzka);
				printf("arr:\n");
				printArray(arr);
			}
			for (i = tah->newx; i < tah->oldx; i++) {
				if (DEBUG) {
					printf("i: %d\n", i);
					printf("arr[tah->oldy][i] %d\n", arr[tah->oldy][i]);
				}
				if (!((arr[tah->oldy][i] == 0) || (arr[tah->oldy][i] == tah->farba))) {
					//ak nie je pravda ze prvok == 0 alebo ID farby
					if (DEBUG) printf("NEPLATI ");
					return 0;
				}
			}
		}
	}
	else {
		//vertikalna kontrola
		if (tah->newy > tah->oldy) {
			//kladny posun
			if (DEBUG) {
				printf("tah->smer: %c\n", tah->smer);
				printf("tah->oldy: %d\n", tah->oldy);
				printf("tah->newy: %d\n", tah->newy);
				printf("tah->oldx: %d\n", tah->oldx);
				printf("tah->newx: %d\n", tah->newx);
				printf("tah->dlzka: %d\n", tah->dlzka);
				printf("arr:\n");
				printArray(arr);
			}
			for (i = tah->oldy; i < tah->newy + tah->dlzka; i++) {
				if (DEBUG) {
					printf("i: %d\n", i);
					printf("arr[i][tah->oldx] %d\n", arr[i][tah->oldx]);
				}
				if (!((arr[i][tah->oldx] == 0) || (arr[i][tah->oldx] == tah->farba))) {
					//ak nie je pravda ze prvok == 0 alebo ID farby
					if (DEBUG) printf("NEPLATI ");
					return 0;
				}
			}
		}
		else {
			//zaporny posun
			if (DEBUG) {
				printf("tah->smer: %c\n", tah->smer);
				printf("tah->oldy: %d\n", tah->oldy);
				printf("tah->newy: %d\n", tah->newy);
				printf("tah->oldx: %d\n", tah->oldx);
				printf("tah->newx: %d\n", tah->newx);
				printf("tah->dlzka: %d\n", tah->dlzka);
				printf("arr:\n");
				printArray(arr);
			}
			for (i = tah->newy; i < tah->oldy; i++) {
				if (DEBUG) {
					printf("i: %d\n", i);
					printf("arr[i][tah->oldx] %d\n", arr[i][tah->oldx]);
				}
				if (!((arr[i][tah->oldx] == 0) || (arr[i][tah->oldx] == tah->farba))) {
					//ak nie je pravda ze prvok == 0 alebo ID farby
					if (DEBUG) printf("NEPLATI ");
					return 0;
				}
			}
		}
	}
	
	//ak to nic ine nevylucuje tak je tento tah mozny
	if (DEBUG) printf("PLATI ");
	return 1;
}

//modifikuje pole podla tahu
int** modifyArray(int **arr, TAH *tah) {
	int i, j;
	int **pole = (int **) malloc (6 * sizeof(int *));
	
	for (i = 0; i < 6; i++) {
		pole[i] = (int *) malloc (6 * sizeof(int));
	}
	
	//okopirovanie hodnot z arr do pole (okrem farby daneho tahu)
	for (i = 0; i < 6; i++) {
		for (j = 0; j < 6; j++) {
			if (arr[i][j] != tah->farba) pole[i][j] = arr[i][j];
			else pole[i][j] = 0;
		}
	}
	
	if (tah->smer == 'h') {
		for (i = 0; i < tah->dlzka; i++) {
			pole[tah->newy][tah->newx + i] = tah->farba;
		}
	}
	else {
		for (i = 0; i < tah->dlzka; i++) {
			pole[tah->newy + i][tah->newx] = tah->farba;
		}
	}
	
	if (DEBUG) {
		printf("pole po vlozeni:\n");
		printArray(pole);
	}
	
	return pole;
}

//hlavny cyklus programu
//Iterative Deepening Search
UZOL* IDS(UZOL *uzol, int maxHlbka, int pocetVozidiel, int cervena) {
	if (DEBUG) printf("Spustenie IDS\n");
	
	int i, j, k;
	TAH *tah = (TAH *) malloc (sizeof(TAH));
	UZOL *vysledok, *novy = (UZOL *) malloc (sizeof(UZOL));
	POZICIA **pozicie = (POZICIA **) malloc (18 * sizeof(POZICIA *));
	
	for (i = 0; i < 18; i++) {
		pozicie[i] = (POZICIA *) malloc (sizeof(POZICIA));
		pozicie[i] = uzol->pozicie[i];
	}
	
	novy->hlbka = uzol->hlbka + 1;
	novy->pred = uzol;
	
	//ak je v maximalnej hlbke
	if (uzol->hlbka == maxHlbka) {
		if (DEBUG) {
			printf("NULL\n");
			printArray(uzol->krizovatka);
		}
		if (checkResult(uzol->krizovatka, cervena) == 1) {
			return uzol;
		}
		else return NULL;
	}
	else {
		if (DEBUG) {
			printf("Nieco robi\n");
			printArray(uzol->krizovatka);	
		}
		//najdi vsetky tahy a vyber iba tie ktore su mozne
		for (i = 0; i < pocetVozidiel; i++) {
			tah->farba = uzol->pozicie[i]->farba;
			tah->dlzka = uzol->pozicie[i]->dlzka;
			tah->smer = uzol->pozicie[i]->smer;
			tah->oldx = uzol->pozicie[i]->x;
			tah->oldy = uzol->pozicie[i]->y;
			
			//vozidlo je horizontalne
			if (uzol->pozicie[i]->smer == 'h') {
				tah->newy = uzol->pozicie[i]->y;
				
				//vyberie vsetky tahy
				for (j = 0; j < 6 - tah->dlzka + 1; j++) {
					if (j != tah->oldx){
						tah->newx = j;
						//vyberie len platne tahy
						if (checkMove(uzol->krizovatka, tah)) {
							//modifikuje krizovatku
							novy->krizovatka = modifyArray(uzol->krizovatka, tah);
							novy->tah = tah;
							
							//modifikuje pozicie vozidiel
							for (k = 0; k < pocetVozidiel; k++) {
								if (k != i) {
									pozicie[k] = uzol->pozicie[k];
								}
								else{
									pozicie[k]->dlzka = uzol->pozicie[k]->dlzka;
									pozicie[k]->farba = uzol->pozicie[k]->farba;
									pozicie[k]->smer = uzol->pozicie[k]->smer;
									pozicie[k]->y = uzol->pozicie[k]->y;
									pozicie[k]->x = j;
								}
							}
							
							novy->pozicie = pozicie;
							
							vysledok = IDS(novy, maxHlbka, pocetVozidiel, cervena);
							
							if (vysledok != NULL) {
								if (DEBUG) printf("IDS vracia vysledok\n");
								return vysledok;
							}
						}
					}
				}
			}
			//vozidlo je vertikalne
			else {
				tah->newx = uzol->pozicie[i]->x;
				
				//vyberie vsetky tahy
				for (j = 0; j < 6 - tah->dlzka + 1; j++) {
					if (j != tah->oldy){
						tah->newy = j;
						//vyberie len platne tahy
						if (checkMove(uzol->krizovatka, tah)) {
							//modifikuje krizovatku
							novy->krizovatka = modifyArray(uzol->krizovatka, tah);
							novy->tah = tah;
							
							//modifikuje pozicie vozidiel
							for (k = 0; k < pocetVozidiel; k++) {
								if (k != i) {
									pozicie[k] = uzol->pozicie[k];
								}
								else{
									pozicie[k]->dlzka = uzol->pozicie[k]->dlzka;
									pozicie[k]->farba = uzol->pozicie[k]->farba;
									pozicie[k]->smer = uzol->pozicie[k]->smer;
									pozicie[k]->x = uzol->pozicie[k]->x;
									pozicie[k]->y = j;
								}
							}
							
							novy->pozicie = pozicie;
							
							vysledok = IDS(novy, maxHlbka, pocetVozidiel, cervena);
							
							if (vysledok != NULL) {
								if (DEBUG) printf("IDS vracia vysledok\n");
								return vysledok;
							}
						}
					}
				}
			}
		}
	}
	
	return NULL;
}

int main() {
	int i=0, j=0, cervena=0, pom[4], hlbka=0;
	char c, *token, *line, farby[18][30] = { "\0" };
	UZOL *uzol, *vysledok = NULL;
	POZICIA **pozicie;
	
	pozicie = (POZICIA **) malloc (18 * sizeof(POZICIA*));
	for (i = 0; i < 18; i++) {
		pozicie[i] = (POZICIA *) malloc (sizeof(POZICIA));
	}
	
	line = (char *) malloc (100);
	uzol = (UZOL *) malloc (sizeof (UZOL));
	uzol->pozicie = (POZICIA **) malloc (18 * sizeof(POZICIA *));
	uzol->hlbka = 0;
	uzol->tah = NULL;
	uzol->pred = NULL;
	uzol->krizovatka = (int **) malloc (6 * sizeof(int *));
	for (i = 0; i < 6; i++) {
		uzol->krizovatka[i] = (int *) malloc (6 * sizeof(int));
	}
	
	i = 0;
	init(uzol->krizovatka, 6, 6);
	
	printf("Zadaj farby vozidiel (max 18), kazdu na samostatny riadok.\n");
	printf("Nacitavanie ukoncis textom \"OK\".\n");
	
	while (fgets(line, 100, stdin) > 0) {
		if (strstr(line, "OK") != NULL) break;
		strncpy(farby[i], line, strstr(line, "\n") - line);
		printf("ID %d: %s\n", i+1, farby[i]);
		if (!strcmp(farby[i], "Cervena") ||
			!strcmp(farby[i], "Cervene") ||
			!strcmp(farby[i], "cervene") ||
			!strcmp(farby[i], "cervena")) cervena = i+1;
		i++;
	}
	
	if (cervena == 0) {
		printf("Nebolo zadane cervene vozidlo, program skonci!");
		return 0;
	}
	
	memset(line, '\0', 100);
	printf("Nacitavanie farieb ukoncene!\n");
	printf("Zadaj pozicie vozidiel.\n");
	printf("Format vstupu (X a Y su od 1 po 6 vratane)\nID_farby dlzka_vozidla X Y h/v\n");
	printf("Nacitavanie ukoncis textom \"OK\".\n");
	j = 0;
	
	while (fgets(line, 100, stdin) > 0) {
		if (strstr(line, "OK") != NULL) break;
		for (i = 0; i < 4; i++) pom[i] = 0;
		i = 0;
		
		token = strtok(line, " ");
		
		while (token != NULL) {
			if (i < 4) {
				pom[i] = atoi(token);
			}
			else {
				c = token[0];
				pozicie[j]->farba = pom[0];
				pozicie[j]->dlzka = pom[1];
				pozicie[j]->x = pom[2] - 1;
				pozicie[j]->y = pom[3] - 1;
				pozicie[j]->smer = c;
				uzol->pozicie = pozicie;
					
				j++;
				
				if (c == 'h') {
					printf("Vozidlo farby %s, dlzky %d je na pozicii %d X %d horizontalne.\n",
					farby[pom[0] - 1], pom[1], pom[2], pom[3]);
					
					for (i = (pom[2] - 1); i < (pom[2] + pom[1] - 1); i++) {
						uzol->krizovatka[pom[3] - 1][i] = pom[0];
					}
				}
				else if (c == 'v') {
					printf("Vozidlo farby %s, dlzky %d je na pozicii %d X %d vertikalne.\n",
					farby[pom[0] - 1], pom[1], pom[2], pom[3]);
					
					for (i = pom[3] - 1; i < pom[3] + pom[1] - 1; i++) {
						uzol->krizovatka[i][pom[2] - 1] = pom[0];
					}
				}
			}
			
			token = strtok(NULL, " ");
			i++;
		}
	}
	
	int pocetVozidiel = j;
	
	printf("Nacitavanie pociatocneho stavu ukoncene!\n");
	
	printArray(uzol->krizovatka);
	
	if (checkResult(uzol->krizovatka, cervena) == 1) {
		printf("Pociatocny stav je zaroven aj koncovy.\n");
		return 0;
	}
	else {
		printf("Zadaj hlbku do ktorej hladat\n");
		scanf("%d", &hlbka);
		printf("Spustam hladanie koncoveho stavu do hlbky %d.\n", hlbka);
	}
	
	//postupne sa prehlbujuce prehladavanie do hlbky
	for (i = 0; i < hlbka; i++) {
		vysledok = IDS(uzol, i, pocetVozidiel, cervena);
		if (vysledok != NULL) {
			printf("V hlbke %d sa nasiel vysledok!\n\n", i+1);
			UZOL* akt = vysledok;
			
			printf("Vysledne pole:\n");
			printArray(akt->krizovatka);
			putchar('\n');
			
			while (akt != NULL) {
				if (DEBUG) {
					printf("akt->tah->smer: %c\n", akt->tah->smer);
					printf("akt->tah->farba: %d\n", akt->tah->farba);
					printf("akt->tah->oldy: %d\n", akt->tah->oldy);
					printf("akt->tah->newy: %d\n", akt->tah->newy);
					printf("akt->tah->oldx: %d\n", akt->tah->oldx);
					printf("akt->tah->newx: %d\n", akt->tah->newx);
					printf("akt->tah->dlzka: %d\n", akt->tah->dlzka);
				}
				
				printf("(%s [%d] ", farby[akt->tah->farba - 1], akt->tah->farba);
				//printf("(%s %d %d\n", farby[akt->tah->farba - 1], akt->tah->newx - akt->tah->oldx, akt->tah->newy - akt->tah->oldy);
				if (akt->tah->smer == 'h') {
					if (akt->tah->newx - akt->tah->oldx > 0) {
						printf("doprava %d)", akt->tah->newx - akt->tah->oldx);
					}
					else {
						printf("dolava %d)", akt->tah->oldx - akt->tah->newx);
					}
				}
				else {
					if (akt->tah->newy - akt->tah->oldy > 0) {
						printf("dole %d)", akt->tah->newy - akt->tah->oldy);
					}
					else {
						printf("hore %d)", akt->tah->oldy - akt->tah->newy);
					}
				}
				
				if (akt->pred == NULL) {
					break;
				}
				
				akt = akt->pred;
			}
			
			return 0;
		}
		else {
			printf("V hlbke %d sa nenachadza vysledok\n", i+1);
		}
	}
	
	return 0;
}
