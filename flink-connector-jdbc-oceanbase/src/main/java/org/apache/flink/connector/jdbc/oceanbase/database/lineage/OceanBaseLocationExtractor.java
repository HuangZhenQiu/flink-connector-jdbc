/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.connector.jdbc.oceanbase.database.lineage;

import org.apache.flink.annotation.Internal;
import org.apache.flink.connector.jdbc.lineage.JdbcLocation;
import org.apache.flink.connector.jdbc.lineage.JdbcLocationExtractor;
import org.apache.flink.connector.jdbc.lineage.OverrideJdbcLocationExtractor;

import java.net.URISyntaxException;
import java.util.Properties;

/**
 * Implementation of {@link JdbcLocationExtractor} for OceanBase.
 *
 * @see <a
 *     href="https://en.oceanbase.com/docs/common-oceanbase-connector-j-en-10000000000381036">OceanBase
 *     URL Format</a>
 */
@Internal
public class OceanBaseLocationExtractor implements JdbcLocationExtractor {

    private static final String PROTOCOL_PART = "^[\\w+:]+://";

    private JdbcLocationExtractor delegate() {
        return new OverrideJdbcLocationExtractor("oceanbase");
    }

    @Override
    public boolean isDefinedAt(String jdbcUri) {
        return delegate().isDefinedAt(jdbcUri);
    }

    @Override
    public JdbcLocation extract(String rawUri, Properties properties) throws URISyntaxException {
        // Schema could be 'oceanbase', 'oceanbase:hamode'. Convert it to 'mysql'
        String normalizedUri = rawUri.replaceFirst(PROTOCOL_PART, "oceanbase://");
        return delegate().extract(normalizedUri, properties);
    }
}