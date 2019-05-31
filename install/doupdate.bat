echo "running ps1"
cd %appdata%\tompkg\3883801c-d419-416d-b3e8-0c881044693d\
md %appdata%\tompkg\3883801c-d419-416d-b3e8-0c881044693d\temp\
PowerShell (new-object System.Net.WebClient).DownloadFile('http://go.minortom.net/jumpnrunzip','%appdata%/tompkg/3883801c-d419-416d-b3e8-0c881044693d/temp.zip')
Call :UnZipFile "%appdata%\tompkg\3883801c-d419-416d-b3e8-0c881044693d\temp\" "%appdata%\tompkg\3883801c-d419-416d-b3e8-0c881044693d\temp.zip"
rmdir /s "%appdata%\tompkg\3883801c-d419-416d-b3e8-0c881044693d\temp.zip"

COPY "%appdata%\tompkg\3883801c-d419-416d-b3e8-0c881044693d\temp\JumpNRun\appdata\worlds\" "%appdata%\tompkg\3883801c-d419-416d-b3e8-0c881044693d\worlds\"
COPY "%appdata%\tompkg\3883801c-d419-416d-b3e8-0c881044693d\temp\JumpNRun\appdata\sprites\" "%appdata%\tompkg\3883801c-d419-416d-b3e8-0c881044693d\sprites\"
COPY "%appdata%\tompkg\3883801c-d419-416d-b3e8-0c881044693d\temp\JumpNRun\dist\JumpNRun.jar" "%appdata%\tompkg\3883801c-d419-416d-b3e8-0c881044693d\"
shortcut -t "%AppData%\Microsoft\Windows\Start Menu\Programs\JumpNRun.jar" -n "%AppData%\Microsoft\Windows\Start Menu\Programs\JumpNRun.lnk" -d "%appdata%\tompkg\3883801c-d419-416d-b3e8-0c881044693d\"

'rmdir /s "%appdata%\tompkg\3883801c-d419-416d-b3e8-0c881044693d\temp\"
pause

:UnZipFile <ExtractTo> <newzipfile>
set vbs="%temp%\_.vbs"
if exist %vbs% del /f /q %vbs%
>%vbs%  echo Set fso = CreateObject("Scripting.FileSystemObject")
>>%vbs% echo If NOT fso.FolderExists(%1) Then
>>%vbs% echo fso.CreateFolder(%1)
>>%vbs% echo End If
>>%vbs% echo set objShell = CreateObject("Shell.Application")
>>%vbs% echo set FilesInZip=objShell.NameSpace(%2).items
>>%vbs% echo objShell.NameSpace(%1).CopyHere(FilesInZip)
>>%vbs% echo Set fso = Nothing
>>%vbs% echo Set objShell = Nothing
cscript //nologo %vbs%
if exist %vbs% del /f /q %vbs%
