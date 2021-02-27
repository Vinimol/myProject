<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<body>
	<h2>Statements</h2>


	<table border="1">
		<tr>
		<th>Number</th>
		<th>Account Id</th>
		<th>Account Type</th>		
		<th>Account Number</th>
		<th>Date</th>
		<th>Amount</th>

		</tr>
		<c:forEach var="emp" items="${listEmp}" varStatus="status">
			<tr>
				<td>${status.index + 1}</td>
				<td>${emp.accountId}</td>
				<td>${emp.accountType}</td>
				<td>${emp.accNumber}</td>
				<td>${emp.date}</td>
				<td>${emp.amount}</td>

			</tr>
		</c:forEach>
	</table>

</body>
</html>