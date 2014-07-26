
How to run tests automatically (note: not from browser manually).

1. Prerequisites
	- node.js installed on your computer
2. Install node modules
	- from "testrunner" directory run the next command: "npm install" (this will install node modules in the same directory)

3. Start the server (node.js)
	- from "client" directory run the next command: "node .\testrunner\scripts\web-server.js"
	
4. Run tests

4.1 Run end2end tests continuously 
	- from "testrunner" directory run the next command: "scripts\e2e-test.bat" (it will open a chrome browser)

4.1 Run unit tests continuously 
	- from "testrunner" directory run the next command: "scripts\test.bat" (it will open a chrome browser)

