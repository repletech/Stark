# STARK

**Description:** Soap services are quite heavy weight and can be tedious to work with. Though these services are still important assets to many organizations it would be much easier to work with JSON rather than working with SOAP which is xml based( its no fun! ).
With **Stark** - A lightweight java service you can easily consume SOAP web services and transform it into REST APIs. Stark enables you to provide a SOAP WSDL URL and it returns you it's REST transformed end-points.


### Prerequisites

A working SOAP Service which is formally described using WSDL.

### How it Works?

User needs to provide the SOAP service URL as an input. **Stark** will fetch all the operations(methods) available in the service and will list it down with the parameters available under each method.
**Stark** returns a unique dynamic URL for each available method. Pass on the parameters and use it to call your SOAP service as REST. This will hence return the result in JSON format.

### API Details:

    host : localhost:8080

#### 1) /explore 
>   Explore webservice, provide list of operations(methods) available in the service. With the details of how to call that operation from the REST API.

**Request Data Details :**
1. name - group name (*User created*)
1. wsdlUrl  - WSDL webservice url (*SOAP service URL*)


URL : http://{{host}}/stark/api/v1/explore

Method Type : POST

Request Payload:
```
{
  "name":"calculator",
  "wsdlUrl": "http://www.dneonline.com/calculator.asmx?WSDL"
}
```

**Reponse Data Details :**

- url -  This is the dynamically generated REST API URL. Use this to call the SOAP service.
- listOfParams - List of API query parameters available in the SOAP service method. These parameters are **case-insensitive.**


**Response**
```
{
    "name": "calculater",
    "wsdlUrl": "http://www.dneonline.com/calculator.asmx?WSDL",
    "operations": [
        {
            "name": "Add",
            "url": "http://localhost:8080/stark/api/v1/execute/calculator/add",
            "listOfParams": [
                "intA",
                "intB"
            ]
        },
        {
            "name": "Divide",
            "url": "http://localhost:8080/stark/api/v1/execute/calculator/divide",
            "listOfParams": [
                "intA",
                "intB"
            ]
        },
        {
            "name": "Multiply",
            "url": "http://localhost:8080/stark/api/v1/execute/calculator/multiply",
            "listOfParams": [
                "intA",
                "intB"
            ]
        },
        {
            "name": "Subtract",
            "url": "http://localhost:8080/stark/api/v1/execute/calculator/subtract",
            "listOfParams": [
                "intA",
                "intB"
            ]
        }
    ]
}
```



[![](https://gitlab.com/fynd/SARS/blob/develop/src/main/resources/photos/explore.png)](https://gitlab.com/fynd/SARS/blob/develop/src/main/resources/photos/explore.png)








#### 2) /execute  
>   To fetch the data from SOAP service, pass the required parameters as you would do for usual REST calls in the Stark generated URL.

**Request Data Details :**
1. These parameters can be understood in the explore section. Each method/operation will have its own parameters. Please refer to them and pass accordingly. 

Method Type : GET

URL :
```
http://localhost:8080/stark/api/v1/execute/calculator/add?inta=10&intb=20
```
**Request-headers :**
> X-Transformation: (Boolean type). Default value is false, hence it would give the original response in JSON. If value is set to true, WSDL details would be removed from the response. Ex. below

**Reponse :**

When: X-Transformation : false
```
{
    "soap:Envelope": {
        "xmlns:xsd": "http://www.w3.org/2001/XMLSchema",
        "xmlns:soap": "http://www.w3.org/2003/05/soap-envelope",
        "xmlns:xsi": "http://www.w3.org/2001/XMLSchema-instance",
        "soap:Body": {
            "AddResponse": {
                "xmlns": "http://tempuri.org/",
                "AddResult": 30
            }
        }
    }
}
```


When: X-Transformation : true
```
{
    "AddResponse": {
        "AddResult": 30
    }
}
```





## License

GNU General Public License v3.0 - see the [LICENSE.md](LICENSE.md)

