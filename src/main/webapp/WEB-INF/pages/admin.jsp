<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%><html>
<body>
  <h3>ROLE : ${message}</h3>
  
	<body>
        <h3>Welcome, Enter The Details to get the statements</h3>
        <form id="fetch" action="fetch" method="post">

             
                
                      <label>Account Id: </label>
  					  <input type="text" name="accId">
  					  <br/>
  					   <label>Date From: </label>
  					  <input type="text" name="dateFrom">
  					  <br/>
  					   <label>Date To: </label>
  					  <input type="text" name="dateTo">
  					  <br/>
  					   <label>Amount From: </label>
  					  <input type="text" name="amountFrom">
  					  <br/>
  					  <label>Amount To: </label>
  					  <input type="text" name="amountTo">
   					  <br/>
   					  <button>Submit</button>
               
           
        </form>
    </body>
	
	<label>Important: ${validations}</label> 
	<br/>
	<a href="<c:url value="j_spring_security_logout" />" > Logout</a>
</body>
</html>