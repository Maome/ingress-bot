Ingress Bot
===========

Author: Maome (Reilly Steele)

Ingress Bot is a java desktop client for the Google game 'Ingress'.

Two files are required:
* authcookie: contains your SACSID cookie from the m-dot-betaspike.appspot.com servers
* locations: has a list of location information in GPS format indicating the waypoints to visit in order

Quick Start Guide:

* Log in to your Google account, point your browser at this URL with Firebug/Dev tools running:  https://m-dot-betaspike.appspot.com/handshake
* Copy the SACSID cookie into the "authcookie" file.  See the authcookie.example file
* Start capturing packets for Ingress Intel page:  $ sudo tcpdump -i eth0 host www.ingress.com -s 65535 -w portals.pcap 
* Log in to www.ingress.com/intel and surf around your city.  Zoom & pan and capture as many portals as you can
* Install "tshark" command line app
* Run:  $ utils/portal\_list.pl portals.pcap > locations  
* See the --help and --man for the script for extra options
* Install Oracle Java SDK
* Run:  $ make
* Run:  $ make run

Currently the bot will begin at the first location in the waypoint list, survey the surrounding area and attempt
to acquire items from each portal in range. Then a timer will be set that will simulate the time it would take to
walk to the next location in the list. At this point the bot will send new location data to the server and again
survey and attempt to acquire items from the portal ('hack' it) this loop continues until each waypoint has been
visited. 

To Do:
* Error handling of valid (but error notifying) return json strings (ie "error":"TOO_SOON_BIG"
* Create loop allowing the bot to run autonomously indefinitely without reaching portal burnout.
* Create clientwrapper functions to allow bursting, linking, and deploying of and on portals.
* Scan local chat for words related to current bot state or actions and enter silent cooldown period.
* Improve google maps implementation to not reach non-api rate limit.
* Add item drop functionality
* Add multi account functionality
