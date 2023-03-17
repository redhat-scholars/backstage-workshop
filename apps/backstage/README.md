# Backstage Setup (Helm on OpenShift 4-12)

This guide borrowed heavily from https://janus-idp.io/blog/deploying-backstage-onto-openshift-using-helm

Thanks to @sabre1041 for the tips!

1. Find a cluster: CRC, RHPDS, BYO environment
2. Install a recent release of `oc` on your dev machine...
3. Install a recent release of `helm` on your dev machine...
4. Log in to your cluster and copy the login command link
5. Paste the login command into your local shell: `oc login ...`
6. Create a namespace for backstage: `oc new-project backstage`
7. Add the following helm repos:
```
helm repo add bitnami https://charts.bitnami.com/bitnami
helm repo add backstage https://backstage.github.io/charts
```
8. Update the `ingress.host` attribute in `values-openshift.yaml`, setting it to the address where backstage will be exposed as a route.  For example: `backstage.apps.cluster-pxsc6.pxsc6.sandbox784.opentlc.com`
9. Install Backstage: `helm install backstage backstage/backstage -f values-openshift.yaml`
10. Open Backstage: https://backstage.apps.$OPENSHIFT_APPS_SUBDOMAIN

For a 1 hour test cluster that includes all of the above, try: https://play.instruqt.com/openshift/tracks/backstage
