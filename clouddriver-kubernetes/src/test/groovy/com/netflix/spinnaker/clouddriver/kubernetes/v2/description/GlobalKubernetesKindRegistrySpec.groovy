/*
 * Copyright 2019 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.netflix.spinnaker.clouddriver.kubernetes.v2.description

import com.netflix.spinnaker.clouddriver.kubernetes.v2.description.manifest.*
import spock.lang.Specification
import spock.lang.Subject

class GlobalKubernetesKindRegistrySpec extends Specification {
  static final KubernetesApiGroup CUSTOM_API_GROUP =  KubernetesApiGroup.fromString("test")
  static final KubernetesKindProperties REPLICA_SET = new KubernetesKindProperties(KubernetesKind.REPLICA_SET, true, true, true)
  static final KubernetesKindProperties CUSTOM_KIND = new KubernetesKindProperties(KubernetesKind.from("customKind", CUSTOM_API_GROUP), true, true, true)

  void "an empty registry returns no kinds"() {
    given:
    @Subject GlobalKubernetesKindRegistry kindRegistry = new GlobalKubernetesKindRegistry([])

    when:
    def kinds = kindRegistry.getRegisteredKinds()

    then:
    kinds.isEmpty()
  }

  void "getRegisteredKinds returns all registered kinds"() {
    given:
    @Subject GlobalKubernetesKindRegistry kindRegistry = new GlobalKubernetesKindRegistry([
      REPLICA_SET,
      CUSTOM_KIND
    ])

    when:
    def kinds = kindRegistry.getRegisteredKinds()

    then:
    kinds.size() == 2
    kinds.contains(REPLICA_SET)
    kinds.contains(CUSTOM_KIND)
  }

  void "getRegisteredKind returns kinds that have been registered"() {
    given:
    @Subject GlobalKubernetesKindRegistry kindRegistry = new GlobalKubernetesKindRegistry([
      REPLICA_SET,
      CUSTOM_KIND
    ])

    when:
    def properties = kindRegistry.getRegisteredKind(KubernetesKind.from("customKind", CUSTOM_API_GROUP))

    then:
    properties == CUSTOM_KIND
  }

  void "getRegisteredKind default properties for a kind that has not been registered"() {
    given:
    @Subject GlobalKubernetesKindRegistry kindRegistry = new GlobalKubernetesKindRegistry([
      REPLICA_SET,
      CUSTOM_KIND
    ])

    when:
    def properties = kindRegistry.getRegisteredKind(KubernetesKind.from("otherKind", CUSTOM_API_GROUP))

    then:
    properties == KubernetesKindProperties.withDefaultProperties(KubernetesKind.from("otherKind", CUSTOM_API_GROUP))
  }
}
