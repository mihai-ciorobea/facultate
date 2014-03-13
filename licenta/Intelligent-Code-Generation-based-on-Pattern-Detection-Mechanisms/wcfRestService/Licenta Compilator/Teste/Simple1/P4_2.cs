using System.Collections.Generic;
using System.Data;
using System.Data.SqlClient;
using Catalyst.Data.Entities.CatalystTime;
using Catalyst.SharedComponents;
using System;
using Catalyst.Data.Entities;
using Catalyst.Utils;

namespace Catalyst.Data.Repositories.Comments
{
    public class CommentRepository : StatelessSiteRepositoryBase, ICommentRepository
    {
        public IList<Comment> GetCommentsByType(int siteId, int objectId, Common.Types objectType)
        {
            IList<Comment> result = SitesDatabase(siteId).Execute
            (
                "spr_CMS_RatingUsageRetrieveByObject",
                CreateInstance,
                SiteIdSqlParameter(siteId),
                new SqlParameter("@ObjectID", objectId),
                new SqlParameter("@ObjectType", objectType)
            );

            return result;
        }

      
    }
}