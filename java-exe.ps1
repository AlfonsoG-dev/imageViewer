$compile = "javac -d .\bin\ .\src\*.java "
$createJar = "jar -cfm imageViewer.jar Manifesto.txt -C .\bin\ ."
$javaCommand = "java -jar imageViewer.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand
