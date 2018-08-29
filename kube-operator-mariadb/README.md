# kube-operator-mariadb

I started the mariadb operator with the goal of provisioning standardized and stable mariadb clusters within a Kubernetes environment, primarily focusing on reducing the complexity of highly-available deployment provisioning and configuration. Why not use a helm chart? Simply put, I wanted more than the point-and-shoot helm deployment strategy offers. There are cases when complex decision making needs to happen and I would rather that happen in code as opposed to bash scripting embedded in a deployment spec or container entrypoint script.

Using code based deployments we can make intelligent decisions when scaling the cluster up and down. We can also constantly inspect the cluster and eventually take action when remediation is required. The code based deployment also allows us to declare our database in a simple custom resource definition, which can and should be part of our continuous integration pipeline. The following custom resource definition can be used to declare a database cluster.

### Database Spec

The `Database` spec allows us to very simply declare that we require a database. In the above specification, we are requesting 3 nodes. The operator uses this specification as the basis for all provisioning operations executed.

```
apiVersion: mariadb.operator.flyover.com/v1
kind: Database
metadata:
  name: mariadb
  namespace: demo
spec:
  clusterId: mariadb-demo
  replicas: 3
``` 

In addition to the above spec fields, we can also include a `nodeSelector` section to target our database pods to particular cluster nodes. For instance we might supply something like this to provision 3 cluster members in location `l1`:

```
apiVersion: mariadb.operator.flyover.com/v1
kind: Database
metadata:
  name: mariadb
  namespace: demo
spec:
  clusterId: mariadb-demo
  nodeSelector:
    loc: l1
  replicas: 3
```

In situations where we would like to have members of our cluster reside in multiple locations, we can use the `bootstrap` attribute to signify that the cluster should not be initialized from scratch, but rather join an existing cluster. The cluster that is joined will be represented by the `clusterId` attribute. A multi-site cluster could potentially have the following specifications.

``` 
apiVersion: mariadb.operator.flyover.com/v1
kind: Database
metadata:
  name: mariadb-l1
  namespace: demo
spec:
  clusterId: mariadb-demo
  nodeSelector:
    loc: l1
  replicas: 3

---

apiVersion: mariadb.operator.flyover.com/v1
kind: Database
metadata:
  name: mariadb-l2
  namespace: demo
spec:
  clusterId: mariadb-demo
  nodeSelector:
    loc: l2
  replicas: 2
  bootstrap: false
```

### Components Created

When a cluster is created the operator creates quite a few Kubernetes components. Each cluster member will be represented by a deployment. This allows us to independently control the configuration for each member of the cluster. 

In addition to the member pods, a secret is created that contains the root password information. This secret can be used by applications in the namespace that need to connect to the data source. Any user account secret will have the name pattern `clusterId-username`. The secret will contain both the username and password data items.

The operator will also provision a ServiceAccount, Role and RoleBinding in the namespace. This is used to support the operator's companion pod that runs along side the first pod in the cluster. This companion pod can be used to provision schemas, users and grants in your cluster. See the section on the `DatabaseUser` spec for more information on this.

Finally, a Service will be created to simplify access to the cluster members. You can simply use the service cluster IP address, or the dns label provided by the service if you have cluster level DNS services installed.

### DatabaseUser Spec

The `DatabaseUser` spec is a custom resource definition that is consumed by the operator's companion pod. This companion pod runs along side the first member in the cluster and is used to provision schemas, users and grants within the database. To Define a user and schema to grant access to, you would create a spec in your namespace like the following.

```
apiVersion: mariadb.operator.flyover.com/v1
kind: DatabaseUser
metadata:
  name: mariadb-demo-foo
  namespace: demo
spec:
  clusterId: mariadb-demo
  user:
    grants:
    - privileges:
      - all
      schema: database-name
    name: foo
```

In the above case, a new schema would be provisioned called `database-name`. The user account `foo` would be created and granted all privileges on that schema. The equivilent commands would be `create schema if not exists database-name;`, `create user 'foo'@'%' identified by '{complex_password}'` and `grant all on database-name.* to 'foo'@'%'`

This not only creates all the required elements in the database, but also provisions a Secret in the namespace to reflect the credentials of the newly created account. In this case a secret named `mariadb-demo-foo`. This secret can then be used by pods that require access to the new database.