/*
 *    Copyright 2018 Horácio Flávio Comé Júnior
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and limitations under the License.
 */

package io.github.horaciocome1.reeque.data

import io.github.horaciocome1.reeque.data.post.PostDAO
import io.github.horaciocome1.reeque.data.topic.TopicDAO
import io.github.horaciocome1.reeque.data.user.UserDAO

class Database private constructor() {

    var topicDAO = TopicDAO()
    var userDAO = UserDAO()
    var postDAO = PostDAO()

    companion object {
        @Volatile private var instance: Database? = null
        fun getInstance() = instance ?: synchronized(this) {
            instance ?: Database().also { instance = it }
        }
    }

}