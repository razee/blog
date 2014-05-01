(ns sanitycheck.models.schema)


(def db
   (or (System/getenv "DATABASE_URL")
              "postgresql://localhost:5432/sanitycheck"))
(def dblocal
  {:classname "org.postgresql.Driver" ; must be in classpath
           :subprotocol "postgresql"
           :subname "//localhost:5432/sanitycheck"
           ; Any additional keys are passed to the driver
           ; as driver-specific properties.
           :user "razi"})
          ; :password "secret"})

