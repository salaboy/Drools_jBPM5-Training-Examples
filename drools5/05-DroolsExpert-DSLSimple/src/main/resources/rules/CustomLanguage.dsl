[condition][]There is an Order from an {type} Customer=order : Order($customer : customer, customer.type=="{type}")
[condition][]- with an amount that is more or equal than {amount}=amount >= {amount}
[condition][]- with an amount that is less than {amount}=amount < {amount}
[condition][]- the shipping is {shipping}=shipping=="{shipping}"
[condition][]- not insured=insured==false
[condition][]Single order=ArrayList( size == 0 ) from collect( Order( customer == $customer) )
[condition][]Has more than one order=ArrayList( size > 0 ) from collect( Order( customer == $customer) )
[consequence][]Add to rejected national list=rejectedNational.add(order);
[consequence][]Add to rejected international list=rejectedInternational.add(order);
[consequence][]Add to priority customer list=priorityCustomer.add(order);
[consequence][]Log : "{message}"=System.out.println("{message}");
