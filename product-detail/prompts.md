1- Estructura el proyecto con packages para controllers, services, repositories, entities, exceptions y config
2- Agrega las dependencias para trabajar con  Prometheusy  Zipkin
3- Creame las entidades segun la estructura del siguiente json:
{
        "id": 1,
        "productId": "MLA123456789",
        "title": "iPhone 14 Pro Max 256GB",
        "condition": "new",
        "categoryId": "MLA1055",
        "listingTypeId": "gold_special",
        "siteId": "MLA",
        "price": 999999.99,
        "currencyId": "ARS",
        "availableQuantity": 5,
        "soldQuantity": 150,
        "buyingMode": "buy_it_now",
        "status": "active",
        "permalink": "https://articulo.mercadolibre.com.ar/MLA-123456789-iphone-14-pro-max-256gb-_JM",
        "thumbnail": "https://http2.mlstatic.com/D_Q_NP_123456-MLA123456789-123456-O.webp",
        "pictures": [
            "[\"https://http2.mlstatic.com/D_Q_NP_123456-MLA123456789-123456-O.webp\",\"https://http2.mlstatic.com/D_Q_NP_789012-MLA123456789-789012-O.webp\"]"
        ],
        "attributes": [
            {
                "id": 1,
                "name": "Marca",
                "valueName": "Apple"
            },
            {
                "id": 2,
                "name": "Modelo",
                "valueName": "iPhone 14 Pro Max"
            },
            {
                "id": 3,
                "name": "Almacenamiento",
                "valueName": "256 GB"
            },
            {
                "id": 4,
                "name": "Color",
                "valueName": "Space Black"
            },
            {
                "id": 5,
                "name": "Sistema operativo",
                "valueName": "iOS"
            }
        ],
        "shipping": [
            {
                "id": 1,
                "freeShipping": true,
                "logisticType": "fulfillment",
                "mode": "me2"
            }
        ],
        "sellers": [
            {
                "id": 1,
                "nickname": "TechStore_AR",
                "sellerType": "professional",
                "address": {
                    "id": 1,
                    "city": "Buenos Aires",
                    "state": "CABA",
                    "country": "Argentina",
                    "zipCode": "C1000"
                }
            }
        ]
    }
4- Utiliza las entities y generame los archivos de confguracion configuracion de liquibase, la idea es que cada entidad tenga su propio archivo xml.
5- generame un cargue inicial de datos apartir de un archivo csv.
6- implementa una clase llamada MeliException que me permita responder un error de manera personalizada.
7- crea un DTO llamado MeliExceptionDTO con la siguiente estructura {
    "timestamp": "",
    "status": ,
    "error": "",
    "trace": "",
    "message": "",
    "path": ""
} y utilizado de base para reponder un error de manera estandar. 
8- realiza test unitario sobre todas las clases.
9- documenta todo mi codigo.
10-Mejora la documentacion de Swagger.
11- Implementa unos esterotipos personalizados con los nombres de getAllProductDetails para contener toda la documentacion de Swagger.
12- Optimiza mi codigo(esto lo usaba por metodos no a nivel de todo el proyecto)










