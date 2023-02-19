# Doing the labs on your own (Windows 10 Home) computer with Windows Subsystem for Linux (WSL)

The Windows Subsystem for Linux (WSL) lets you run a GNU/Linux environment (e.g. Ubuntu) directly on Windows, without the overhead of a virtual machine or dualboot setup.

## Step 1. Install WSL

1. Open the Windows Features menu (search for "windows features" or "características de windows" with the search tool) and check the Windows Subsystem for Linux ("Subsistema de Windows para Linux") checkbox. WSL1 is enough for many tasks, so you can just use it. However, recently a WSL2 update has been released. Besides some improvements, you will need to update to WSL2 if you need to use Docker and you have Windows 10 Home. In case you want to update to WSL2 follow instructions in ANNEX 3 before continuing with the next steps. 

2. Once you have WSL enabled, you can already go to the Microsoft Store, search "Ubuntu 20" and install Ubuntu 20.04.LTS. 

*NOTE: It's recommended to enable copy-paste CTRL+SHIFT+C/V in the Ubuntu window settings.*

Check ANNEX 1 and ANNEX 2 to learn how to edit text files that are within WSL.


## ANNEX 1. Accessing the WSL filesystem from Windows applications

Two ways:

a. Run the following from within WSL:
```
	explorer.exe .
```
b. From the File Explorer access the following path:
```
	 \\wsl$
```

## ANNEX 2. Editing the code with a text editor

WSL does not currently support graphical user interfaces (GUIs). One work around is to use an X-server but it's not necessary if the only GUI that you need is a text editor or an IDE. For a text editor you may use [Notepad++](https://notepad-plus-plus.org/downloads/). If you need an IDE, Visual Studio Code is a suitable option as it provides integration with WSL.

*NOTE: In order to access the WSL filesystem from Notepad++ use the path \\\\wsl$*


## ANNEX 3. Update to WSL2

1. Open PowerShell as Administrator and run:

```
	dism.exe /online /enable-feature /featurename:VirtualMachinePlatform /all /norestart
```

2. Restart your machine to complete the WSL install and update to WSL2.

3. Download and run the Linux kernel update package from [here](https://wslstorestorage.blob.core.windows.net/wslblob/wsl_update_x64.msi)

4. Set WSL2 as your default version running the following PowerShell command:

```
	wsl --set-default-version 2
```

*NOTE: Alternative installation instructions [here](https://docs.microsoft.com/en-us/windows/wsl/install-win10)*

If you plan to use Docker with WSL2 read [this](./docker_wsl2.md).


