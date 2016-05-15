package delphi;
/**  
 * <b>This Java Class consists the server side for the wondeful JavaScript
 * library 'sha1.js'.  I wrote it because I basically needed  some cheap
 * client/server login authentication by the usual key/data system. Besides,
 * I got the creeps watching the password posted unencrypted via http requests.
 * This class makes sure that if your client is using the 'sha1.js' to encrypt
 * the password with a key sent by the server, you can always repeat the
 * encrypting on the server side (using the same key) and compare the encrypted
 * strings. Since anyone who is trapping the http requests can actually send
 * you the same encrypted string, I suggest you use the client's IP address as
 * the base for the key generation. Since IP address spoofing is not a problem,
 * this authentication method is not a very secured solution. If you need a
 * full proof solution use ssl. However, this one, sure beats nothing. Feel
 * free to do with it whatever you want</b>  
 * <p><b>This Class is an Abstract Class, to make sure you do not create any
 * new instances of it. It does not throw any exceptions and the code is much
 * more 'C' like than pure object oriented. There are no implemented interfaces
 * and no inheritance in use. In fact, it  is written as close as possible to
 * the original JavaScript code. I did not test tweaking the instance variables
 * but if you do change  them, make sure to apply the same change in the
 * 'sha1.js' library or you won't get the same encrypted strings. You can
 * call each one of the 6 work methods by using something like:
 * SHA1.hex_hmac_sha1("key", "data"); They are the only public methods.
 * All are public and static. You have no reason to call the private ones
 * anyway.</p></b> <p>The 'sha1.js' is a JavaScript implementation of the
 * Secure Hash Algorithm, SHA-1, as defined in FIPS PUB 180-1. JavaScript
 * Version 2.1 Copyright Paul Johnston 2000 - 2002. Other contributors to
 * JavaScript version: Greg Holt, Andrew Kepert, Ydnar, Lostinet Distributed
 * under the BSD License</p>   
 * <p>See <a href="http://pajhome.org.uk/crypt/md5">
   http://pajhome.org.uk/crypt/md5</a> for details.</p>  
 * <p><b>Author: </b>T.N.Silverman (C.T.Xm - SiA Riga, LV)
     <a href="mailto:tnsilver@ctcm.com">mailto:tnsilver@ctxm.com</a>  
 * <br>Creation date: (3/27/2004 5:57:00 PM)</p>  
 * <p>Don't forget to visit my company, <b>CTXM</b> site at
   <a href="http://www.ctxm.com">http://www.ctxm.com</a> where you will
    find reference to all of the games this code is used in.  
 */   

import java.io.*;   
import java.util.*;   
import java.math.*;   
    
    
public abstract class SHA1 {   
  // hex output format. false - lowercase; true - uppercase
  private static final boolean hexcase = false;

  // base-64 pad character. "=" for strict RFC compliance
  private static final String b64pad = "=";

  // bits per input character. 8 - ASCII; 16 - Unicode
  private static final int chrsz = 8;

 /**  
  * This is one of the functions you'll usually want to call
  * It take a string arguments and returns either hex or base-64 encoded strings  
  * Creation date: (3/27/2004 6:05:10 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return java.lang.String  
  * @param key java.lang.String  
  * @param data java.lang.String  
  */   
 public static String b64_hmac_sha1(String key, String data) {   
  return binb2b64(core_hmac_sha1(key, data));   
 }   

 /**  
  * This is one of the functions you'll usually want to call  
  * It take a string argument and returns either hex or base-64 encoded strings  
  * Creation date: (3/27/2004 6:05:10 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return java.lang.String  
  * @param s java.lang.String  
  */   
 public static String b64_sha1(String s) {   
  s = (s==null) ? "" : s;   
  return binb2b64(core_sha1(str2binb(s), s.length() * chrsz));   
 }   

 /**  
  * Convert an array of big-endian words to a base-64 string  
  * Creation date: (3/27/2004 6:05:10 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return java.lang.String  
  * @param binarray int[]  
  */   
 private static String binb2b64(int[] binarray) {   
  String tab = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";   
  String str = "";   
  binarray = strechBinArray(binarray, binarray.length * 4);   
  for (int i = 0; i < binarray.length * 4; i += 3) {   
    int triplet =   
      (((binarray[i >> 2] >> 8 * (3 - i % 4)) & 0xFF) << 16)   
        | (((binarray[i + 1 >> 2] >> 8 * (3 - (i + 1) % 4)) & 0xFF) << 8)   
        | ((binarray[i + 2 >> 2] >> 8 * (3 - (i + 2) % 4)) & 0xFF);   
    for (int j = 0; j < 4; j++) {   
      if (i * 8 + j * 6 > binarray.length * 32)   
        str += b64pad;   
      else   
        str += tab.charAt((triplet >> 6 * (3 - j)) & 0x3F);   
    }   
  }   
  return cleanB64Str(str);   
 }   

 /**  
  * Convert an array of big-endian words to a hex string.  
  * Creation date: (3/27/2004 6:05:10 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return java.lang.String  
  * @param binarray int[]  
  */   
 private static String binb2hex(int[] binarray) {   
  String hex_tab = hexcase ? "0123456789ABCDEF" : "0123456789abcdef";   
  String str = "";   
  for (int i = 0; i < binarray.length * 4; i++) {   
    char a = (char) hex_tab.charAt((binarray[i >> 2] >> ((3 - i % 4) * 8 + 4)) & 0xF);   
    char b = (char) hex_tab.charAt((binarray[i >> 2] >> ((3 - i % 4) * 8)) & 0xF);   
    str += (new Character(a).toString() + new Character(b).toString());   
  }   
  return str;   
 }   

 /**  
  * Convert an array of big-endian words to a string  
  * Creation date: (3/27/2004 6:05:10 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return java.lang.String  
  * @param bin int[]  
  */   
 private static String binb2str(int[] bin) {   
  String str = "";   
  int mask = (1 << chrsz) - 1;   
  for (int i = 0; i < bin.length * 32; i += chrsz)   
    str += (char) ((bin[i >> 5] >>> (24 - i % 32)) & mask);   
  return str;   
 }   

 /**  
  * Bitwise rotate a 32-bit number to the left.  
  * Creation date: (3/26/2004 1:05:01 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return int  
  * @param num int  
  * @param cnt int  
  */   
 private static int bit_rol(int num, int cnt) {   
  return (num << cnt) | (num >>> (32 - cnt));   
 }   
 /**  
  * Cleans a base64 String from all the trailing 'A' or other  
  * characters put there by binb2b64 that made the bin array  
  * 4 times larger than it originally was.  
  * Creation date: (3/27/2004 6:05:10 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return java.lang.String  
  * @param str java.lang.String  
  */   
 private static String cleanB64Str(String str) {   
  str = (str==null) ? "" : str;   
  int len = str.length();   
  if (len <= 1)    
    return str;   
  char trailChar = str.charAt(len - 1);   
  String trailStr="";   
  for (int i=len-1;i>=0 && str.charAt(i)==trailChar;i--)   
     trailStr += str.charAt(i);       
  return str.substring(0,str.indexOf(trailStr));   
 }   

 /**  
  * Makes an int array of a length less than 16 an array of length 16 with all previous  
  * cells at their previous indexes.  
  * Creation date: (3/27/2004 6:05:10 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return int[]  
  * @param str java.lang.String  
  */   
 private static int[] complete216(int[] oldbin) {   
  if (oldbin.length >= 16)   
    return oldbin;   
  int[] newbin = new int[16 - oldbin.length];   
  for (int i = 0; i < newbin.length; newbin[i] = 0, i++);   
  return concat(oldbin, newbin);   
 }   

 /**  
  * Joins two int arrays and return one that contains all the previous values.  
  * This corresponds to the concat method of the JavaScript Array object.  
  * Creation date: (3/27/2004 6:05:10 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0   
  * @return int[]  
  * @param str java.lang.String  
  */   
 private static int[] concat(int[] oldbin, int[] newbin) {   
  int[] retval = new int[oldbin.length + newbin.length];   
  for (int i = 0; i < (oldbin.length + newbin.length); i++) {   
    if (i < oldbin.length)   
      retval[i] = oldbin[i];   
    else   
      retval[i] = newbin[i - oldbin.length];   
  }   
  return retval;   
 }   

 /**  
  * Calculate the HMAC-SHA1 of a key and some data  
  * Creation date: (3/26/2004 1:05:01 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return int  
  * @param x java.lang.String[]  
  * @param len int  
  */   
 private static int[] core_hmac_sha1(String key, String data) {   
  key = (key == null) ? "" : key;   
  data = (data == null) ? "" : data;   
  int[] bkey = complete216(str2binb(key));   
  if (bkey.length > 16)   
    bkey = core_sha1(bkey, key.length() * chrsz);   
  int[] ipad = new int[16];   
  int[] opad = new int[16];   
  for (int i = 0; i < 16; ipad[i] = 0, opad[i] = 0, i++);   
  for (int i = 0; i < 16; i++) {   
    ipad[i] = bkey[i] ^ 0x36363636;   
    opad[i] = bkey[i] ^ 0x5C5C5C5C;   
  }   
  int[] hash =   
    core_sha1(concat(ipad, str2binb(data)), 512 + data.length() * chrsz);   
  return core_sha1(concat(opad, hash), 512 + 160);   
 }   

 /**  
  * Calculate the SHA-1 of an array of big-endian words, and a bit length  
  * Creation date: (3/26/2004 1:05:01 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return int  
  * @param x java.lang.String[]  
  * @param len int  
  */   
 private static int[] core_sha1(int[] x, int len) {   
  /* append padding */   
  int size = (len >> 5);   
  x = strechBinArray(x, size);   
  x[len >> 5] |= 0x80 << (24 - len % 32);   
  size = ((len + 64 >> 9) << 4) + 15;   
  x = strechBinArray(x, size);   
  x[((len + 64 >> 9) << 4) + 15] = len;   
  int[] w = new int[80];   
  int a = 1732584193;   
  int b = -271733879;   
  int c = -1732584194;   
  int d = 271733878;   
  int e = -1009589776;   
  for (int i = 0; i < x.length; i += 16) {   
    int olda = a;   
    int oldb = b;   
    int oldc = c;   
    int oldd = d;   
    int olde = e;   
    for (int j = 0; j < 80; j++) {   
      if (j < 16)   
        w[j] = x[i + j];   
      else   
        w[j] = rol(w[j - 3] ^ w[j - 8] ^ w[j - 14] ^ w[j - 16], 1);   
      int t =   
        safe_add(   
          safe_add(rol(a, 5), sha1_ft(j, b, c, d)),   
          safe_add(safe_add(e, w[j]), sha1_kt(j)));   
      e = d;   
      d = c;   
      c = rol(b, 30);   
      b = a;   
      a = t;   
    }   
    a = safe_add(a, olda);   
    b = safe_add(b, oldb);   
    c = safe_add(c, oldc);   
    d = safe_add(d, oldd);   
    e = safe_add(e, olde);   
  }   
  int[] retval = new int[5];   
  retval[0] = a;   
  retval[1] = b;   
  retval[2] = c;   
  retval[3] = d;   
  retval[4] = e;   
  return retval;   
 }   

 /**  
  * Just a test function to output the results of the 6 working funcions to
  * the standard out.  The two Strings used as parameters are null. Feel free
  * to test with different values.  
  * Creation date:(3/27/2006:05:10PM)  
  * @author T.N.Silverman (20151010 M.DePalma comment package delphi)
  * @version 1.0.0  
  * @return java.lang.String  
  */   
 public static void main(String args[]) {   
     
    long initialTime = System.currentTimeMillis();   
    byte plainText[]= new byte[10240] ;   
       
     
    try {   
  System.out.println("hex_hmac_sha1(pandora,sha1)=" + hex_hmac_sha1("pandora", "sha1"));   
                FileInputStream fileIn = new FileInputStream("1.txt");   
                int bytes = fileIn.read(plainText);   
                String str = new String(plainText, 0, bytes, "Default");   
                System.out.println("file length: "+bytes);   
                System.out.println("hash: "+hex_sha1(str));   
                long difference = System.currentTimeMillis()-initialTime;   
                System.out.println("Elapsed milliseconds: " + difference);   
                fileIn.close();   
                   
            }catch(Exception e){   
                String err=e.toString();   
                System.out.println(err);   
            }   
     
     
  //System.out.println("b64_sha1(" + data + ")=" + b64_sha1(data));   
  //System.out.println("str_sha1(" + data + ")=" + str_sha1(data));   
  //System.out.println("hex_hmac_sha1(" + key + "," + data + ")=" + hex_hmac_sha1(key, data));   
  //System.out.println("b64_hmac_sha1(" + key + "," + data + ")=" + b64_hmac_sha1(key, data));   
  //System.out.println("str_hmac_sha1(" + key + "," + data + ")=" + str_hmac_sha1(key, data));   
 }   

 /** 
  * This is one of the functions you'll usually want to call  
  * It take a string arguments and returns either hex or base-64 encoded strings  
  * Creation date: (3/27/2004 6:05:10 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return java.lang.String  
  * @param key java.lang.String  
  * @param data java.lang.String  
  */   
 public static String hex_hmac_sha1(String key, String data) {   
  return binb2hex(core_hmac_sha1(key, data));   
 }   

 /**  
  * This is one of the functions you'll usually want to call  
  * It take a string argument and returns either hex or base-64 encoded strings  
  * Creation date: (3/27/2004 6:05:10 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return java.lang.String  
  * @param s java.lang.String  
  */   
 public static String hex_sha1(String s) {   
  s = (s == null) ? "" : s;   
  return binb2hex(core_sha1(str2binb(s), s.length() * chrsz));   
 }   

 /**  
  * Bitwise rotate a 32-bit number to the left. * Creation date: (3/26/2004 1:05:01 PM)  
  * Creation date: (3/27/2004 6:05:10 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return int  
  * @param num int  
  * @param cnt int  
  */   
 private static int rol(int num, int cnt) {   
  return (num << cnt) | (num >>> (32 - cnt));   
 }   

 /**  
  * Add ints, wrapping at 2^32. This uses 16-bit operations internally  
  * to work around bugs in some JS interpreters. The original function  
  * is part of the sha1.js library. It's here for compatibility.  
  * Creation date: (3/26/2004 1:05:01 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return int  
  * @param num int  
  * @param cnt int  
  */   
 private static int safe_add(int x, int y) {   
  int lsw = (int) (x & 0xFFFF) + (int) (y & 0xFFFF);   
  int msw = (x >> 16) + (y >> 16) + (lsw >> 16);   
  return (msw << 16) | (lsw & 0xFFFF);   
 }   

 /**  
  * Perform the appropriate triplet combination function for the current  
  * Creation date: (3/26/2004 1:05:01 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return int  
  * @param t int  
  * @param b int  
  * @param c int  
  * @param d int  
  */   
 private static int sha1_ft(int t, int b, int c, int d) {   
  if (t < 20)   
    return (b & c) | ((~b) & d);   
  if (t < 40)   
    return b ^ c ^ d;   
  if (t < 60)   
    return (b & c) | (b & d) | (c & d);   
  return b ^ c ^ d;   
 }   

 /**  
  * Determine the appropriate additive constant for the current iteration  
  * Creation date: (3/26/2004 1:05:01 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return int  
  * @param t int  
  */   
 private static int sha1_kt(int t) {   
  return (t < 20)   
    ? 1518500249   
    : (t < 40)   
    ? 1859775393   
    : (t < 60)   
    ? -1894007588   
    : -899497514;   
 }   

 /**  
  * This is a boolean returnig test function that exists in the sha1.js library.  
  * If it returns 'false' something is wrong.   
  * Creation date: (3/26/2004 1:05:01 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return java.lang.String  
  * @param s java.lang.String  
  */   
 private static boolean sha1_vm_test() {   
  return hexcase ? hex_sha1("abc").equals("A9993E364706816ABA3E25717850C26C9CD0D89D") : hex_sha1("abc").equals("a9993e364706816aba3e25717850c26c9cd0d89d");   
 }   

 /**  
  * This is one of the functions you'll usually want to call  
  * It take a string arguments and returns either hex or base-64 encoded strings  
  * Creation date: (3/26/2004 1:05:01 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return java.lang.String  
  * @param key java.lang.String  
  * @param data java.lang.String  
  */   
 public static String str_hmac_sha1(String key, String data) {   
  return binb2str(core_hmac_sha1(key, data));   
 }   

 /**  
  * This is one of the functions you'll usually want to call  
  * It take a string argument and returns either hex or base-64 encoded strings  
  * Creation date: (3/26/2004 1:05:01 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return java.lang.String  
  * @param s java.lang.String  
  */   
 public static String str_sha1(String s) {   
  s = (s == null) ? "" : s;   
  return binb2str(core_sha1(str2binb(s), s.length() * chrsz));   
 }   

 /**  
  * Convert an 8-bit or 16-bit string to an array of big-endian words  
  * In 8-bit function, characters >255 have their hi-byte silently ignored.  
  * Creation date: (3/26/2004 1:05:01 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return int[]  
  * @param str java.lang.String  
  */   
 private static int[] str2binb(String str) {   
  str = (str==null) ? "" : str;   
  int[] tmp = new int[str.length() * chrsz];   
  int mask = (1 << chrsz) - 1;   
  for(int i = 0; i < str.length() * chrsz; i += chrsz)   
    tmp[i>>5] |= ( (int)(str.charAt(i / chrsz)) & mask) << (24 - i%32);   
   
  int len = 0;   
  for (int i=0;i<tmp.length&&tmp[i]!=0;i++,len++);   
  int[] bin = new int[len];   
  for (int i=0;i<len;i++)   
    bin[i] = tmp[i];   
  return bin;   
 }   

 /**  
  * increase an int array to a desired sized + 1 while keeping the old values.  
  * Creation date: (3/26/2004 1:05:01 PM)  
  * @author T.N.Silverman  
  * @version 1.0.0  
  * @return int[]  
  * @param str java.lang.String  
  */   
 private static int[] strechBinArray(int[] oldbin, int size) {   
  int currlen = oldbin.length;   
  if (currlen >= size + 1)   
    return oldbin;   
  int[] newbin = new int[size + 1];   
  for (int i = 0; i < size; newbin[i] = 0, i++);   
  for (int i = 0; i < currlen; i++)   
    newbin[i] = oldbin[i];   
  return newbin;   
 }   
}
