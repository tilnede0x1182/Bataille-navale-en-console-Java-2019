@echo off

cls&&cd ..\src && javac *.java -d "../class" && cd ../BAT && java -cp "../class" Jeu
cd ../BAT