$srcClasses = "src\application\*.java src\application\client\panels\*.java src\application\client\utils\*.java src\application\mundo\*.java "
$libFiles = ""
$compile = "javac --release 23 -Xlint:all -Xdiags:verbose -d .\bin\ $srcClasses"
$createJar = "jar -cfm ImageViewer.jar Manifesto.txt -C .\bin\ ."
$javaCommand = "java -jar ImageViewer.jar"
$runCommand = "$compile" + " && " + "$createJar" + " && " +"$javaCommand"
Invoke-Expression $runCommand 
