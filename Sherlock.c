#include <stdio.h> 
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <omp.h>
#include <ctype.h>
#define MAX_CHAR 1024
#define WORDQ 2000
#define PROBABILISTIC_PERC 15
#define MUTATION_PERC 12
#define MUTATION_DEC 0.3
#define ALL_COMBINATIONS 65536 //2^16



char *ReadDocument(char *docName, char *text){
	FILE *file;
 	int quantityOfLines = 0;
	int maxLenght = 0;
	char buffer[1024];
 	
 	file = fopen(docName,"r");
 	
 	if (file == NULL){
		printf("Couldn't open the selected file\n");
 		exit(1);
	}
 	else{
		while(!feof(file)){
			fgets(buffer, MAX_CHAR, file);

			quantityOfLines = quantityOfLines+1;
			if(strlen(buffer) > maxLenght){
				maxLenght = strlen(buffer);
			}
		}
		rewind(file);
		text = (char * ) malloc(1 + sizeof(char*) * quantityOfLines * maxLenght);
		
		while (!feof(file)){
			fgets(buffer, MAX_CHAR, file);
			strcat(text,buffer);
		}
	}
	fclose(file);
	return text;
}

int analyzeWord(char word[100]){
	int wordLenght = strlen(word);
	if(strlen(word) < 5){
		return 1;
	}else if(isupper(word[0])){
		return 1;
	}else if(isdigit(word[wordLenght-1])){
		return 1;
	}else if(stricmp(word, "index") == 0){
		return 1;
	}
	else 
		return 0;
}

int searchVariablesErrors(char * text){
	char * currentChar = text;
	char buffer[100];
	int charIndex=0;
	int badNamesQuantity = 0;
	while(*currentChar){
		memset(buffer,0,100);
		if(*currentChar == 'i'){
			*currentChar++;
			if(*currentChar == 'n'){
				*currentChar++;
				if(*currentChar == 't'){
					*currentChar++;
					while(*currentChar == ' '){
						*currentChar++;
					}
					while(*currentChar != ' ' && *currentChar != ';' && *currentChar != '=' &&*currentChar != '(' && *currentChar != ',' && *currentChar != ')' ){
						buffer[charIndex] = *currentChar;
						*currentChar++;
						charIndex++;
					}
					badNamesQuantity = badNamesQuantity + analyzeWord(buffer);
					charIndex = 0;
				}
			}
		}
		else if(*currentChar == 'f'){
			*currentChar++;
			if(*currentChar == 'l'){
				*currentChar++;
				if(*currentChar == 'o'){
					*currentChar++;
					if(*currentChar == 'a'){
						*currentChar++;
						if(*currentChar == 't'){
							*currentChar++;
						}
						while(*currentChar == ' '){
							*currentChar++;
						}
						while(*currentChar != ' ' && *currentChar != ';' && *currentChar != '=' &&*currentChar != '(' && *currentChar != ',' && *currentChar != ')' ){
							buffer[charIndex] = *currentChar;
							*currentChar++;
							charIndex++;
						}
						badNamesQuantity = badNamesQuantity + analyzeWord(buffer);
						charIndex = 0;
					}
				}
			}
		}
		else if(*currentChar == 'd'){
			*currentChar++;
			if(*currentChar == 'o'){
				*currentChar++;
				if(*currentChar == 'u'){
					*currentChar++;
					if(*currentChar == 'b'){
						*currentChar++;
						if(*currentChar == 'l'){
							*currentChar++;
							if(*currentChar == 'e'){
							*currentChar++;
							}
							while(*currentChar == ' '){
								*currentChar++;
							}
							while(*currentChar != ' ' && *currentChar != ';' && *currentChar != '=' &&*currentChar != '(' && *currentChar != ',' && *currentChar != ')' ){
								buffer[charIndex] = *currentChar;
								*currentChar++;
								charIndex++;
							}
							badNamesQuantity = badNamesQuantity + analyzeWord(buffer);
							charIndex = 0;
						}
					}
				}
			}
		}
		else if(*currentChar == 'c'){
			*currentChar++;
			if(*currentChar == 'h'){
				*currentChar++;
				if(*currentChar == 'a'){
					*currentChar++;
					if(*currentChar == 'r'){
						*currentChar++;
					}
					while(*currentChar == ' '){
						*currentChar++;
					}
					while(*currentChar != ' ' && *currentChar != ';' && *currentChar != '=' &&*currentChar != '(' && *currentChar != ',' && *currentChar != ')' ){
						buffer[charIndex] = *currentChar;
						*currentChar++;
						charIndex++;
					}
					badNamesQuantity = badNamesQuantity + analyzeWord(buffer);
					charIndex = 0;
				}
			}
		}
		*currentChar++;
	}
	return badNamesQuantity;
}

int searchMagicNumbers(char * text){
		
	char * currentChar = text;
	char buffer[100];
	int charIndex = 0;
	int magicNumbersFound = 0;
	while(*currentChar){
		memset(buffer,0,100);
		while(isalpha(*currentChar)){
			buffer[charIndex] = *currentChar;
			*currentChar++;
			charIndex++;
		}
		while(*currentChar == ' '){
			*currentChar++;
		}
		if(*currentChar == '='){
			*currentChar++;
			if(*currentChar != '='){
				while(*currentChar == ' '){
					*currentChar++;
				}
				if(*currentChar == '\"' || *currentChar == '\''){
					magicNumbersFound++;
				}else if(isdigit(*currentChar)){
					if(*currentChar != '0'){
						magicNumbersFound++;
					}
				}
			}
		}
		if(!isalpha(*currentChar)){
			*currentChar++;
		}
		charIndex=0;
	}
	return magicNumbersFound;
}

int searchExceptionErrors(char * text){
	char * currentChar = text;
	char buffer[100];
	int charIndex = 0;
	int exceptionErrorsFound = 0;
	int indentQuantity = 0;
	int exceptionFlag = 0;
	while(*currentChar){
		memset(buffer,0,100);
		while(isalpha(*currentChar)){
			buffer[charIndex] = *currentChar;
			*currentChar++;
			charIndex++;
		}
		
		if(stricmp(buffer, "int")==0 || stricmp(buffer, "Double")==0 || 
		stricmp(buffer, "float")==0 || stricmp(buffer, "char")==0 || 
		strcmp(buffer, "void") == 0 ||  strcmp(buffer, "String") == 0 ||
		 strcmp(buffer, "Date") == 0 ||  strcmp(buffer, "boolean") == 0){
			while(*currentChar == ' '){
				*currentChar++;
			}
			while(isalpha(*currentChar) || *currentChar == '*'){
				*currentChar++;
			}
			if(*currentChar == '('){
				while(*currentChar != ')'){
					*currentChar++;
				}
				*currentChar++;
				while(*currentChar == ' '){
					*currentChar++;
				}
				if(*currentChar == '{'){
					indentQuantity++;
					while(*currentChar != '}' && indentQuantity != 0){
						
						while(*currentChar == ' '){
							*currentChar++;
						}
						if(*currentChar == '{'){
							indentQuantity++;
							}
						else if(*currentChar == '}')
							indentQuantity--;
						else{
							charIndex=0;
							memset(buffer,0,100);
							while(isalpha(*currentChar)){
								buffer[charIndex] = *currentChar;
								*currentChar++;
								charIndex++;
							}
							if(stricmp(buffer, "try") == 0){
								exceptionFlag = 1;
							}
						}
						*currentChar++;
					}
					if(exceptionFlag == 0)
						exceptionErrorsFound++;
					exceptionFlag = 0;
				}
			}
		}
		*currentChar++;
		charIndex = 0;
	}
	return exceptionErrorsFound;
}

int searchRepeatedCode(char *text){
	int repeatedCodeCount = 0;
	char * currentChar = text;
	char buffer[100];
	int charIndex = 0;
	int wordQuantity = 0;
	while(*currentChar){
		memset(buffer,0,100);
		while(isalpha(*currentChar)){
			buffer[charIndex] = *currentChar;
			*currentChar++;
			charIndex++;
		}
		if(isalpha(buffer[0])){
			wordQuantity++;
		}
		*currentChar++;
		charIndex=0;
		
	}
	double range = (float)wordQuantity/(float)WORDQ;
	int percentage;
	int partialResult;
	if(range>=1){
		percentage = ceil(PROBABILISTIC_PERC*range);
		percentage = percentage%51;
		if(percentage < 10)
			percentage = PROBABILISTIC_PERC;
	}
	else
		percentage = ceil(PROBABILISTIC_PERC + PROBABILISTIC_PERC*range);
	for(int iterationNum=0; iterationNum<percentage; iterationNum++){
		partialResult = rand()%101;
		if(partialResult <= PROBABILISTIC_PERC){
			repeatedCodeCount++;
		}
	}

	return repeatedCodeCount;
}

int createMask(int bitQuantity, int type){
	int result = 0;
	if(type == 0){
		for(int currentBit = 1; currentBit<=bitQuantity; currentBit++){
			result += pow(2, 16-currentBit);
		}
	}else{
		for(int currentBit = 0; currentBit < bitQuantity; currentBit++){
			result += pow(2, currentBit);
		}
	}
	return result;
}

void evaluate( int InitialVariableErrors, int InitialMagicNumbersFound, int InitialExceptionErrors, 
			int InitialRepeatedCodeFound, int varErr, int magicNum, int excepErr, int repeatNum){
	int sumOfInitialValues = InitialVariableErrors + InitialMagicNumbersFound + InitialExceptionErrors + InitialRepeatedCodeFound;
	int sumOfGeneticValues = varErr + magicNum + excepErr + repeatNum;
	if(sumOfInitialValues*0.15 > fabs(sumOfGeneticValues-sumOfInitialValues)){
		char* problem = "variables";
		int max = varErr;
		if(max < magicNum){
			max = magicNum;
			problem = "numeros magicos";
		}
		if(max < excepErr){
			max = excepErr;
			problem = "excepciones";
		}
		if(max < repeatNum){
			max = repeatNum;
			problem = "codigo repetido";
		}
		printf("\nEl total de errores iniciales fue de %i y al final de las 20 generaciones obtuvo %i.", sumOfInitialValues, sumOfGeneticValues);
		printf("\nNo va a cumplir con el 15 porciento de mejora, por lo tanto va a ser un criminal.\nVa a cometer errores principalmente en %s.\n", problem);
	}else
		printf("\nMejoro mas del 15 porciento, no es un criminal\n");
}

void geneticCode(int population[], int totalPop, int currentGen, int InitialVariableErrors, int InitialMagicNumbersFound, int InitialExceptionErrors, int InitialRepeatedCodeFound){
	int varErr = 0;
	int excepErr = 0;
	int magicNum = 0;
	int repeatNum = 0;
	if(currentGen == 20){
		return;
	}else{
		//FUNCION DE FITNESS
		int groupQuantity = rand()%totalPop;
		int tempGroupQuantity = groupQuantity;
		float selectedGroup[groupQuantity][3];
		int getFromPopulation;
		while(groupQuantity>0){
			groupQuantity--;
			getFromPopulation = population[(rand()%totalPop)];
			if(getFromPopulation <= 14417){
				selectedGroup[groupQuantity][0] = 1; //cantidad de hijos
				selectedGroup[groupQuantity][1] = (float)getFromPopulation / (float)(rand()%101); // resultado de f(I)
				selectedGroup[groupQuantity][2] = getFromPopulation; //valor original
			}
			else if(getFromPopulation <= 26213){
				selectedGroup[groupQuantity][0] = 4;
				selectedGroup[groupQuantity][1] = (float)getFromPopulation / (float)(rand()%101);
				selectedGroup[groupQuantity][2] = getFromPopulation;
			}
			else if(getFromPopulation <= 42597){
				selectedGroup[groupQuantity][0] = 3;
				selectedGroup[groupQuantity][1] = (float)getFromPopulation / (float)(rand()%101);
				selectedGroup[groupQuantity][2] = getFromPopulation;
			}
			else if(getFromPopulation <= 65535){
				selectedGroup[groupQuantity][0] = 2;
				selectedGroup[groupQuantity][1] = (float)getFromPopulation / (float)(rand()%101);
				selectedGroup[groupQuantity][2] = getFromPopulation;
			}
			//else
				
		}
		groupQuantity = tempGroupQuantity;
		int groupBiggerIndex[1][3];
		groupBiggerIndex[0][0] = 0;
		groupBiggerIndex[0][1] = 0;
		groupBiggerIndex[0][2] = 0;
		int secondBiggestIndex[1][3];
		for(int currentIndividual=0; currentIndividual<groupQuantity; currentIndividual++){
			//Obtengo los dos valores mas grandes de f(I) (ademas obtengo la cantidad de hijos dependiendo del tipo y su valor inicial)
			if(selectedGroup[currentIndividual][1]>groupBiggerIndex[0][1]){
				secondBiggestIndex[0][0] = groupBiggerIndex[0][0];
				secondBiggestIndex[0][1] = groupBiggerIndex[0][1];
				secondBiggestIndex[0][2] = groupBiggerIndex[0][2];
				

				groupBiggerIndex[0][0] = selectedGroup[currentIndividual][0];
				groupBiggerIndex[0][1] = selectedGroup[currentIndividual][1];
				groupBiggerIndex[0][2] = selectedGroup[currentIndividual][2];
			}
		}
		
		//Obtener la cantidad de hijos
		int childrenQuantity = 0;
		if(groupBiggerIndex[0][0]>secondBiggestIndex[0][0])
			childrenQuantity = groupBiggerIndex[0][0];
		else
			childrenQuantity = secondBiggestIndex[0][0];
		
		//Aplicar la mascara a los valores obtenidos de la funcion de fitness para crear los hijos y ver si muta o no
		int bitQuantity;
		int hiMask;
		int lowMask;
		int isMutation=0;
		int newChildren[childrenQuantity];
		for(int currenChild=0; currenChild<childrenQuantity; currenChild++){
			
			bitQuantity = rand()%15+1;
			hiMask = createMask(bitQuantity, 0);
			lowMask = createMask(16-bitQuantity, 1);
			newChildren[currenChild] = (groupBiggerIndex[0][2] & hiMask) + (secondBiggestIndex[0][2] & lowMask);
			if(rand()%101 <= (MUTATION_PERC-(currentGen*MUTATION_DEC))){ //MUTA
				int bitMutation = rand()%16;
				int bitMask = 65535 - pow(2, bitMutation);
				if(newChildren[currenChild] & bitMask == newChildren[currenChild]){ //si el bit buscado es un 0 se le suma ese bit
					newChildren[currenChild] = newChildren[currenChild] + pow(2, bitMutation); 
				}else
					newChildren[currenChild] = newChildren[currenChild] & bitMask; //si es un 1, se hace un AND con un 0 en la posicion deseada
			}
		}
		
		int newPopulation[totalPop + childrenQuantity];
		
		for(int newPopIndex=0; newPopIndex<totalPop; newPopIndex++){
			newPopulation[newPopIndex] = population[newPopIndex];
			
			if(newPopulation[newPopIndex] >= 1442 && newPopulation[newPopIndex] <= 12976){
				varErr++;
			}
			else if(newPopulation[newPopIndex] >= 15599 && newPopulation[newPopIndex] <= 25033){
				magicNum++;
			}
			else if(newPopulation[newPopIndex] >= 27855 && newPopulation[newPopIndex] <= 40957){
				excepErr++;
			}
			else if(newPopulation[newPopIndex] >= 44893 && newPopulation[newPopIndex] <= 63241){
				repeatNum++;
			}
		}
		for(int newPopIndex=0; newPopIndex<childrenQuantity; newPopIndex++){
			newPopulation[totalPop+newPopIndex] = newChildren[newPopIndex];
			if(newPopulation[totalPop+newPopIndex] >= 1442 && newPopulation[totalPop+newPopIndex] <= 12976){
				varErr++;
			}
			else if(newPopulation[totalPop+newPopIndex] >= 15599 && newPopulation[totalPop+newPopIndex] <= 25033){
				magicNum++;
			}
			else if(newPopulation[totalPop+newPopIndex] >= 27855 && newPopulation[totalPop+newPopIndex] <= 40957){
				excepErr++;
			}
			else if(newPopulation[totalPop+newPopIndex] >= 44893 && newPopulation[totalPop+newPopIndex] <= 63241){
				repeatNum++;
			}
		}
				
		geneticCode(newPopulation, totalPop+childrenQuantity, currentGen+1,
		InitialVariableErrors, InitialMagicNumbersFound, InitialExceptionErrors, InitialRepeatedCodeFound);
	}
	 if(currentGen == 19){
		evaluate(InitialVariableErrors, InitialMagicNumbersFound, InitialExceptionErrors,
		InitialRepeatedCodeFound, varErr, magicNum, excepErr, repeatNum);
	}
}

int main(int argc, char *argv[])
{
	char *fileText;
	int variableErrors = 0;
	int magicNumbersFound = 0;
	int exceptionErrors = 0;
	int repeatedCodeFound = 0;

	#pragma omp parallel for
	for(int fileIndex=1; fileIndex<argc; fileIndex++){
		fileText = ReadDocument(argv[fileIndex], fileText);

		#pragma omp task
		variableErrors += searchVariablesErrors(fileText);
		#pragma omp task
		magicNumbersFound += searchMagicNumbers(fileText);
		#pragma omp task
		exceptionErrors += searchExceptionErrors(fileText);
		#pragma omp task
		repeatedCodeFound += searchRepeatedCode(fileText);
	}
	
	

	#pragma omp taskwait
	free(fileText);
	//GENOTIPO
	int varWeight[2];
	varWeight[0] = 0;
	varWeight[1] = (ceil(ALL_COMBINATIONS * 0.22))-1; // 0-14417 (14.417,92)
	int magicNumWeight[2];
	magicNumWeight[0] = varWeight[1]+1;
	magicNumWeight[1] = magicNumWeight[0]+floor(ALL_COMBINATIONS * 0.18)-1; //14418-26213 (11.796,48)
	int excepErrWeight[2];
	excepErrWeight[0] = magicNumWeight[1]+1;
	excepErrWeight[1] = excepErrWeight[0]+(ALL_COMBINATIONS * 0.25)-1; // 26214-42597 (16.384)
	int repeatWeight[2];
	repeatWeight[0] = excepErrWeight[1]+1;
	repeatWeight[1] = repeatWeight[0]+ceil(ALL_COMBINATIONS * 0.35)-1; // 42598-65535 (22.937,6)

	//CREACION DE LA POBLACION POBLACION
	int totalPop = variableErrors+magicNumbersFound+exceptionErrors+repeatedCodeFound;
	int population[totalPop];
	int generalIndividual=0;
	int rangeValues = varWeight[1];
	for(int currentIndividual=0; currentIndividual<variableErrors; currentIndividual++){
		population[generalIndividual] = rand()%rangeValues;
		generalIndividual++;
	}
	rangeValues=magicNumWeight[1]-magicNumWeight[0];
	for(int currentIndividual=0; currentIndividual<magicNumbersFound; currentIndividual++){
		population[generalIndividual] = magicNumWeight[1]-rand()%rangeValues;
		generalIndividual++;
	}
	rangeValues = excepErrWeight[1] - excepErrWeight[0];
	for(int currentIndividual=0; currentIndividual<exceptionErrors; currentIndividual++){
		population[generalIndividual] = excepErrWeight[1]-rand()%rangeValues;
		generalIndividual++;
	}
	rangeValues = repeatWeight[1]-repeatWeight[0];
	for(int currentIndividual=0; currentIndividual<repeatedCodeFound; currentIndividual++){
		population[generalIndividual] = repeatWeight[1]-rand()%rangeValues;
		generalIndividual++;
	}

	geneticCode(population, totalPop, 0, variableErrors,magicNumbersFound,exceptionErrors,repeatedCodeFound);


	
	system("pause");
	
	return 0;
}
