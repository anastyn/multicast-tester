### MULTICAST Tester application
Allows to test sending and receiving of MULTICAST text messages in your network.
Supports multiple senders and receivers.

What is IP multicast: https://en.wikipedia.org/wiki/IP_multicast

Supported parameters:

1. --sender `(optional)`

   Run the application in Sender mode. Will allow to send new messages to the joined group.

2. --group=[group_ip]

   The multicast group. Read more here: http://www.tldp.org/HOWTO/Multicast-HOWTO-2.html

3. --port=[port_number]

   The port number. Random by default.

4. --interface=[interface] `(optional)`

   Useful on hosts with multiple network interfaces, if you want to use other than the system default.

5. --ttl=[number] `(optional)`

   Controls the scope of the multicasts. Should be between 0 and 255.
   The default value is 1, which limits the scope of the messages to the hosts subnet.
   The list of possible values:
   <pre>
        0 Restricted to the same host. Won't be output by any interface.
        1 Restricted to the same subnet. Won't be forwarded by a router.
      <32 Restricted to the same site, organization or department.
      <64 Restricted to the same region.
     <128 Restricted to the same continent.
     <255 Unrestricted in scope. Global.
   </pre>

To run the application as a sender:
<pre>
java -jar multicast-tester-1.0-SNAPSHOT.jar --sender --group=225.0.0.0 --port=59343
</pre>

To run it as a receiver:
<pre>
java -jar multicast-tester-1.0-SNAPSHOT.jar --group=225.0.0.0 --port=59343
</pre>
