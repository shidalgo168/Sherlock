#include <stdio.h> 
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <omp.h>
#include <ctype.h>
#define MAX_CHAR 1024
#define WORDQ 2000
#define PROBABILISTIC_PERC 15



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

}

int main(int argc, char *argv[])
{
	char *fileText;
	int variableErrors = 0;
	int magicNumbersFound = 0;
	int exceptionErrors = 0;
	int repeatedCodeFound = 0;

	for(int fileIndex=1; fileIndex<argc; fileIndex++){
		fileText = ReadDocument(argv[fileIndex], fileText);

		#pragma omp parallel
		variableErrors += searchVariablesErrors(fileText);
		#pragma omp parallel
		magicNumbersFound += searchMagicNumbers(fileText);
		#pragma omp parallel
		exceptionErrors += searchExceptionErrors(fileText);
		#pragma omp parallel
		repeatedCodeFound += searchRepeatedCode(fileText);
	}
	printf("%i\n", variableErrors);
	printf("%i\n", magicNumbersFound);
	printf("%i\n", exceptionErrors);
	printf("%i\n", repeatedCodeFound);

	geneticCode(variableErrors, magicNumbersFound, exceptionErrors, repeatedCodeFound);

	
	system("pause");
	free(fileText);
	return 0;
}
