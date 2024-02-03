$compile = "javac -Xlint:all -d .\bin\ .\src\*.java .\src\Interface\Panels\*.java .\src\Mundo\*.java "
$createJar = "jar -cfm imageViewer.jar Manifesto.txt -C .\bin\ ."
$javaCommand = "java -jar imageViewer.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand
