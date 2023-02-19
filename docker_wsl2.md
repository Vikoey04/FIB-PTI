# Using Docker within (WSL2) Ubuntu within Windows 

## Step 1. Ensure that you have WSL2 for Windows installed

Open a PowerShell terminal and run:
```
	wsl -l -v
```
You should see something like this:

```
  NAME                   STATE           VERSION
* Ubuntu-20.04           Running         2
```

If you still haven't install WSL2 follow instructions [here](./wsl.md)

## Step 2. Install and configure Docker (in Windows)

1. Download the Windows Docker installer from [here](https://www.docker.com/get-started).

2. Open Docker settings/Resources/WSL Integration and enable "integration with additional distros: Ubuntu-20.04"

Now, if you open an Ubuntu terminal, you should be able to run docker commands.