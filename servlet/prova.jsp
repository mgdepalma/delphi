<html>
<head>
<title>capture in javascript</title>
<script src="script/jquery-1.11.3.js"></script>
<script>
 var ajax;

 $.get('oracle?request=setting&name=seed&redirect=no', function(data) {
   ajax = data;
 });

 function prova()
 {
  alert( ajax );
 }
</script>
<body onload="prova()">
</body>
</html>
