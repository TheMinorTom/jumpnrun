setlocal enableextensions enabledelayedexpansion
md %appdata%/tompkg/3883801c-d419-416d-b3e8-0c881044693d/
PowerShell (new-object System.Net.WebClient).DownloadFile('https://go.minortom.net/jumpnruninstallps1','%appdata%/tompkg/3883801c-d419-416d-b3e8-0c881044693d/install.ps1')
PowerShell %appdata%/tompkg/3883801c-d419-416d-b3e8-0c881044693d/install.ps1
pause
