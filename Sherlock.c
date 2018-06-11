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

void geneticCode(int varErr, int magicNum, int excepErr, int repeatNum){
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

	int population[varErr+magicNum+excepErr+repeatNum];
	int generalIndividual=0;
	int rangeValues = varWeight[1];
	for(int currentIndividual=0; currentIndividual<varErr; currentIndividual++){
		population[generalIndividual] = rand()%rangeValues;
		generalIndividual++;
	}
	rangeValues=magicNumWeight[1]-magicNumWeight[0];
	for(int currentIndividual=0; currentIndividual<magicNum; currentIndividual++){
		population[generalIndividual] = magicNumWeight[1]-rand()%rangeValues;
		generalIndividual++;
	}
	rangeValues = excepErrWeight[1] - excepErrWeight[0];
	for(int currentIndividual=0; currentIndividual<excepErr; currentIndividual++){
		population[generalIndividual] = excepErrWeight[1]-rand()%rangeValues;
		generalIndividual++;
	}
	rangeValues = repeatWeight[1]-repeatWeight[0];
	for(int currentIndividual=0; currentIndividual<repeatNum; currentIndividual++){
		population[generalIndividual] = repeatWeight[1]-rand()%rangeValues;
		generalIndividual++;
	}

	for(int i=0; i<generalIndividual; i++){
		printf("%i\n", population[i]);
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
	geneticCode(variableErrors, magicNumbersFound, exceptionErrors, repeatedCodeFound);

	
	system("pause");
	free(fileText);
	return 0;
}
