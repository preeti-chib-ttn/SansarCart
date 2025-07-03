# Sansar Cart: JVM Bootcamp project
### `Preeti Chib`

[Database Diagram and flows](https://excalidraw.com/#json=Xz_FbnsrdcXIQZnC_aU_h,fLSlNcEm0yRTEpg57bvu6A)



## Database Relations:
#### User Role:
    - Owner: user
    - Many to Many
    - unidirectional
#### Address User:
    - Owner: Address
    - Many to One
    - unidirectional
#### Category Category:
    - Owner: Category
    - Many to One
    - unidirectional
#### Product Category:
    - Owner: Product
    - Many to One
    - Unidirectional
#### Product-Variation Product:
    - Owner: Product Variation
    - Many to One (many product variation can exist for one product)
    - Bidirectional

### Product-Review Product
    - Owner: Product Review
    - Many to One (Many review can exist for one product)
    - Unidirectional (review needed) [Bidirectional is better]

### CategoryMetaDataValue - Category & CategoryMetaDataField
    - Owner: CategoryMetaDataValue
    - Embedded Id: Category and categoryMetaDataField
    - Relation: Many to one
    - Bidirectional
    - Persist Merge
    - Orphan removal true

### Cart - Product Variation
    - Owner: Cart
    - Relation: Many to One
    - Many cart item can contain same product variation
    - Unidirectional

### Cart - Customer 
    - Owner: Cart [need to be in embedded id]
    - Relation: Many to one
    - Many cart item can be associated with one customer
    - Unidirectional


## Overall:

### Image storage: 
    /upload folder: as this is better than resources we can easily move 

